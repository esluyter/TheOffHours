OHProgramView {
  var program, renderPath, s;
  var win, view, timeline;
  var startTime = 0.0, duration = 54000;
  var playhead = 0.0;
  var <>playFunc;
  var playing = false;
  var playpoint = 0.0;
  var playpointRout;
  var buf;

  *new { |program, renderPath, irDir, s|
    ^super.newCopyArgs(program, renderPath, s).init(irDir);
  }

  refresh {
    view.refresh;
    timeline.refresh;
  }

  init { |irDir|
    var clips, deletedClips, birdClips;
    var refreshClips = {
      clips = program.clips.select { |clip| ((clip.startTime >= startTime) or: ((clip.startTime + clip.duration) >= startTime)) and: (clip.startTime < ((startTime + duration))) };
      deletedClips = program.deletedClips.select { |clip| ((clip.startTime >= startTime) or: ((clip.startTime + clip.duration) >= startTime)) and: (clip.startTime < ((startTime + duration))) };
      birdClips = program.birdClips.select { |clip| ((clip.startTime >= (startTime)) or: ((clip.startTime + clip.duration) >= (startTime))) and: (clip.startTime < ((startTime + duration))) }; //ugh
    };
    var refreshView = {
      view.refresh;
      timeline.refresh;
    };

    var secsToPixels = { |secs| secs / duration * view.bounds.width };
    var pixelsToSecs = { |pixels| pixels / view.bounds.width * duration };
    var pixelsToPercent = { |pixels| pixels / view.bounds.width };
    var secsToPercent = { |secs| secs / duration };

    var clickPoint, clickTime, originalDuration, prDrawSpectrograms = true;

    var scrollRefreshRout;



    refreshClips.();

    win = Window("The Off Hours", Window.availableBounds).front;
    timeline = UserView(win, win.bounds.copy.origin_(0@0).height_(35)).background_(Color.white)
    .drawFunc_({
      Pen.use {
        var sunsetPx = secsToPixels.(program.suntimes.sunsetSeconds - startTime);
        var sunrisePx = secsToPixels.(program.suntimes.sunriseSeconds - startTime);
        Pen.color = Color.gray(0, 0.5);
        Pen.addRect(Rect(sunsetPx, 0, sunrisePx - sunsetPx, 35));
        Pen.fill;

        (duration / 60).asInteger.do { |i|
          var time = i * 60 + (((startTime / 60.0).ceil * 60) - startTime);
          var realTime = startTime + time;
          var hour, minutes;
          var x = secsToPixels.(time);
          var orderOfMagnitude = duration.lincurve(0.5 * 60 * 60, 1.5 * 60 * 60, 1, 2, 0.2, nil);
          #hour, minutes = OffHoursScore.seconds2TimeOfDay(realTime);
          Pen.color = Color.black;
          //Pen.addRect(Rect(x, 0, 3, 20));
          //Pen.fill;
          if (i % orderOfMagnitude.ceil.asInteger == 0) {
            if ((minutes - minutes.asInteger) == 0) {
              Pen.addRect(Rect(x - 1, 0, 1, 35));
              Pen.fill;
              Pen.stringAtPoint(hour.asString ++ ":" ++ minutes.asInteger.asString.padLeft(2, "0"), (x + 2)@(0), Font("Courier New", 12));
            } {
              var seconds = minutes - minutes.asInteger * 60;
              //Pen.stringAtPoint(":" ++ seconds.asInteger, (x + 10)@(image.height - 35), Font("Courier New", 25));
            }
          };
        };
      }
    })
    .mouseDownAction_({ |v, x, y, mods|
      clickPoint = x@y;
      clickTime = pixelsToSecs.(x) + startTime;
      originalDuration = duration;
      prDrawSpectrograms = false;
    })
    .mouseUpAction_({ |v, x, y, mods|
      clickPoint = nil;
      clickTime = nil;
      originalDuration = nil;
      prDrawSpectrograms = true;
      refreshClips.();
      refreshView.();
    })
    .mouseMoveAction_({ |v, x, y, mods|
      var yDelta = y - clickPoint.y;
      var xDelta = x - clickPoint.x;
      if (mods.isAlt) { // hold option to scroll in opposite direction
        yDelta = yDelta.neg;
      };
      //yDelta.postln;
      duration = (originalDuration * yDelta.linexp(-100, 100, 0.5, 2, nil)).clip(0, 15 * 60 * 60);
      startTime = (clickTime - pixelsToSecs.(clickPoint.x));
      startTime = (xDelta.linlin(0, v.bounds.width, startTime, startTime - duration, nil));
      startTime = startTime.clip(0, 15 * 60 * 60);
      refreshClips.();
      refreshView.();
    });
    view = UserView(win, win.bounds.copy.origin_(0@35).height_(win.bounds.height - 35)).background_(Color.white)
    .drawFunc_({
      Pen.use {
        program.birdClips.select({ |clip| ((clip.startTime <= (startTime + duration)) and: (clip.endTime >= startTime)) }).do { |clip|
          var height = view.bounds.height;
          var top = 0;
          var left = secsToPixels.(clip.startTime - startTime);
          var width = secsToPixels.(clip.duration);
          var tmpImg;
          Pen.color = Color.fromHexString("e2c80c");

          Pen.addRect(Rect(left, top, width, height));
          Pen.fill;

          if (duration < (2.5 * 60 * 60) and: prDrawSpectrograms) {
            tmpImg = Image.open(clip.imagePath);
            Pen.use {
              Pen.translate(left, top);
              Pen.scale(width / tmpImg.width, height / tmpImg.height);
              Pen.drawImage(0@0, tmpImg);
            };
            tmpImg.free;
          };

          if (duration <= (5 * 60 * 60)) {
            Pen.stringAtPoint(clip.name.asString, (left)@(height - 50));
          };
        };

        clips.select({ |clip| ((clip.startTime <= (startTime + duration)) and: (clip.endTime >= startTime)) }).do { |clip|
          var chan = clip.speaker;
          var height = view.bounds.height / 4;
          var top = height * chan;
          var left = secsToPixels.(clip.startTime - startTime);
          var width = secsToPixels.(clip.duration);
          var grayAmt = clip.modeIndex.linlin(0, 5, 0.9, 0.6);
          var tmpImg;

          Pen.color = Color.gray(grayAmt.linlin(0.6, 0.9, 0.4, 0.95), 0.5);
          if (clip.type == \grandChord) {
            Pen.color = Color.fromHexString("a80000").alpha_(0.5);
            if (duration > (5 * 60 * 60)) {
              width = max(width, 2);
            };
          };
          if (clip.type == \shadowChord) {
            Pen.color = Color.fromHexString("0f00b9").alpha_(0.5);
          };
          Pen.addRect(Rect(left, top, width, height));
          Pen.fill;

          if (duration < (2.5 * 60 * 60) and: prDrawSpectrograms) {
            tmpImg = Image.open(clip.imagePath);
            Pen.use {
              Pen.translate(left, top);
              Pen.scale(width / tmpImg.width, height / tmpImg.height);
              Pen.drawImage(0@0, tmpImg);
            };
            tmpImg.free;
          };

          if (duration <= (5 * 60 * 60)) {
            Pen.color = if (grayAmt < 0.5) { Color.white } { Color.black };
            Pen.rotate(pi/2, left, top);
            Pen.stringAtPoint(clip.type.asString ++ " / " ++ clip.mode.asString, (left)@(top - 15));
            Pen.rotate(-pi/2, left, top);
          };
        };

        deletedClips.select({ |clip| ((clip.startTime <= (startTime + duration)) and: (clip.endTime >= startTime)) }).do { |clip|
          var chan = clip.speaker;
          var height = view.bounds.height / 4;
          var top = height * chan;
          var left = secsToPixels.(clip.startTime - startTime);
          var width = secsToPixels.(clip.duration);
          var grayAmt = clip.modeIndex.linlin(0, 5, 0.9, 0.6);
          //var tmpImg = Image.open(clip.imagePath);

          Pen.color = Color.gray(grayAmt);
          Pen.addRect(Rect(left, top, width, height));
          Pen.stroke;

          /*
          Pen.use {
          Pen.translate(left, top);
          Pen.scale(width / tmpImg.width, height / tmpImg.height);
          Pen.drawImage(0@0, tmpImg);
          };
          tmpImg.free;
          */

          //Pen.color = if (grayAmt < 0.5) { Color.white } { Color.black };
          if (duration <= (5 * 60 * 60)) {
            Pen.rotate(pi/2, left, top);
            Pen.stringAtPoint(clip.type.asString, (left)@(top - 15));
            Pen.rotate(-pi/2, left, top);
          };
        };

        Pen.color = Color.black;
        Pen.width = 3;
        Pen.addRect(Rect(secsToPixels.(program.dawnChorusStartTime - startTime), 0, secsToPixels.(program.dawnChorusDuration), view.bounds.height - 29));
        Pen.stroke;
        Pen.addRect(Rect(secsToPixels.(program.duskChorusStartTime - startTime), 0, secsToPixels.(program.duskChorusDuration), view.bounds.height - 29));
        Pen.stroke;

        Pen.addRect(Rect(secsToPixels.(playhead - startTime), 0, 2, view.bounds.height));
        Pen.fill;

        if (playing != false) {
          Pen.color = Color.red;
          Pen.addRect(Rect(secsToPixels.(playpoint - startTime), 0, 2, view.bounds.height));
          Pen.fill;
        };
      };
    })
    .mouseDownAction_({ |v, x, y, modifiers|
      playhead = startTime + pixelsToSecs.(x);
      refreshView.();
    })
    .mouseWheelAction_({ |v, x, y, modifiers, xDelta, yDelta|
      var clickTime = pixelsToSecs.(x) + startTime;
      duration = (duration * yDelta.linexp(-100, 100, 0.5, 2, nil)).clip(0, 15 * 60 * 60);
      startTime = (clickTime - pixelsToSecs.(x));
      startTime = ((xDelta * 2).linlin(0, v.bounds.width, startTime, startTime - duration, nil));
      startTime = startTime.clip(0, 15 * 60 * 60);

      prDrawSpectrograms = false;
      refreshClips.();
      refreshView.();
      scrollRefreshRout.stop;
      scrollRefreshRout = fork {
        0.1.wait;
        prDrawSpectrograms = true;
        defer {
          refreshClips.();
          refreshView.();
        };
      };
    })
    .keyDownAction_({ |v, char, modifiers, unicode, keycode, key|
      if (char == $ ) {
        if (playing == false) {
          this.play;
        } {
          this.stop;
        };
      };
    });
  }

  stop {
    playpointRout.stop;
    playing.free;
    playing = false;
  }

  play { |pos|
    if (pos.notNil) {
      playhead = pos;
    };
    s.waitForBoot {
      this.prLoadBuf;
      s.sync;
      this.prPlayBuf;
    };
  }

  prLoadBuf { buf = Buffer.cueSoundFile(s, renderPath, (playhead.postln * 48000).postln, 4); }
  prPlayBuf {
    playing = playFunc.(buf);
    playpoint = playhead;
    playpointRout = fork {
      inf.do {
        0.5.wait;
        playpoint = playpoint + 0.5;
        defer {
          this.refresh;
        };
      };
    };
  }
}