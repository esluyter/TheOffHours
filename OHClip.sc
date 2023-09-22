OHClip {
  var <cycle, <type, <>startTime, <>duration, <startTimeInCycle, <index, <name, <weight;

  classvar nextNodeID = 1001;

  *new { |cycle, type, startTime, duration, index, name = \stuart, weight = 1.0|
    ^super.newCopyArgs(cycle, type, startTime, duration, startTime, index, name, weight).init;
  }

  init {

  }

  score { ^cycle.score }
  speaker { ^cycle.speaker }

  *modes { ^[\lydian, \ionian, \mixolydian, \dorian, \aeolian, \phrygian]; }
  mode { ^this.class.modes[this.modeIndex]; }
  modeIndex { ^this.score.modeIndexAtTime(startTime) }

  endTime { ^(startTime + duration) }

  startFrame { ^this.score.sampleRate * startTime }
  numFrames { ^this.score.sampleRate * duration }

  speakerName { ^("sp" ++ (this.speaker + 1)); }

  audioPath {
    if (type == \birdsong) {
      ^(cycle.audioPath +/+ this.name.asString ++ ".wav");
    } {
      ^(cycle.audioPath +/+ /*this.mode*/ "lydian" +/+ this.speakerName ++ ".wav") // <---------- this is temporary!!!!!!!!!
    };
  }
  imagePath {
    if (type == \birdsong) {
      ^(cycle.imagePath +/+ this.name.asString ++ ".tiff");
    } {
      ^(cycle.imagePath +/+ /*this.mode*/ "lydian" +/+ this.speakerName ++ "_" ++ this.index ++ ".tiff") // <---------- this is temporary!!!!!!!!!
    };
  }

  asString {
    if (type == \birdsong) {
      ^("<" ++ [this.name ++ " -> " ++ this.weight, startTime->this.endTime].join(" | ") ++ ">");
    } {
      ^("<" ++ [this.speakerName, this.mode ++ " / " ++ type, startTime->this.endTime].join(" | ") ++ ">");
    };
  }

  printOn { |stream| stream << this.asString; }

  addToScore { |score, server|
    var x;
    startTime.postln;
    score.add([startTime, [\b_close, this.speaker]]);
    score.add([startTime, [\b_read, this.speaker, this.audioPath, startTimeInCycle * 48000, 32768, 0, 1].postln]);
    score.add([startTime, (x = Synth.basicNew(\stream, server, nextNodeID)).newMsg(args: [buf: this.speaker, out: this.speaker])]);
    score.add([this.endTime, x.freeMsg]);

    nextNodeID = nextNodeID + 1;
  }

  generateImage {
    var image, tmp_images = nil!2;

    var sampleRate = this.score.sampleRate;
    var hops = 4;

    // analyze audio : this will take a while to run
    (2048 * [4, 1]).do { |size, imageIndex|

      //var size = 4096 * 0.5;

      var window = Signal.hanningWindow(size.postln);
      var imag = Signal.newClear(size);
      var cosTable = Signal.fftCosTable(size);

      var data = Signal.read(this.audioPath, this.numFrames, this.startFrame);

      var numFrames = (data.size / size * hops);
      var fftMags = Array(numFrames);

      ("processing cycle " ++ cycle.speaker ++ " clip " ++ index).postln;

      (data.size / size * hops).asInteger.do { |j|
        var i = j / hops;
        var r = data[(size * i).asInteger..(size * (i + 1) - 1).asInteger];
        r = r.addAll(Array.fill(size - r.size, {0}));
        r = r * window;
        fftMags.add((fft(r, imag, cosTable).magnitude.log10)[0..(size * 20000/sampleRate).asInteger]);
      };

      ("done processing cycle " ++ cycle.speaker ++ " clip " ++ index).postln;
      ("generating image for cycle " ++ cycle.speaker ++ " clip " ++ index).postln;

      {
        var width = fftMags.size;
        var height = fftMags[0].size;
        var image = Image.new(width, height);
        var colorFunc = if (type == \birdsong) { // <------------ replace with logic to detect bird call
          { |mag| Color.gray(0, mag.lincurve(-2, 1, 0, 1, curve: 1.5)) };
        } {
          { |mag| Color.gray(0, mag.lincurve(-2.5, 1.3, 0, 1, curve: 0)) }
        };
        var scaleFunc = { |pixel| pixel.lincurve(0, height, 1, height, curve: 3) }; // curve factor for Y scaleing (0 = linear, 3 =~ mel scale)

        image.setPixels(Int32Array.fill(width * height, { |i|
          var index = scaleFunc.(height - (i / width) - 1).asInteger;
          var frame = i % width;
          var mag = fftMags[frame][index];
          Image.colorToPixel(colorFunc.(mag));
        }), Rect(0, 0, image.width, image.height));

        tmp_images[imageIndex] = image;
      }.();

      ("done generating image for  cycle " ++ cycle.speaker ++ " clip " ++ index).postln;
    };

    image = Image.new(tmp_images[0].width, tmp_images[0].height);
    image.draw { |image|
      Pen.use {
        Pen.drawImage(0@0, tmp_images[0]);
        Pen.scale(tmp_images[0].width / tmp_images[1].width, tmp_images[0].height / tmp_images[1].height);
        Pen.drawImage(0@0, tmp_images[1]);
      }
    };

    image.write(this.imagePath);

    tmp_images.do(_.free);
    image.free;
  }
}