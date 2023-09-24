OHBirdsong {
  var <score;
  var <clips;

  *new { |score, arr|
    ^super.newCopyArgs(score).init(arr);
  }

  init { |arr|
    clips = [];
    arr.do { |assoc, index|
      var name = assoc.key;
      var weight = assoc.value;
      var duration = 1.0;
      var clip = OHClip(this, \birdsong, 0.0, duration, index, name, weight);
      var sf = SoundFile.openRead(clip.audioPath);
      duration = sf.numFrames / 48000;
      sf.close;
      clip.duration = duration;
      clips = clips.add(clip);
    }
  }

  randomBird {
    var weights = clips.collect(_.weight).normalizeSum;
    ^clips.wchoose(weights);
  }

  generateImages {
    clips.do(_.generateImage);
  }

  addBuffersToScore { |score|
    clips.do(_.addBufferToScore(score));
  }

  speaker { ^0 }

  audioPath { ^(score.audioPath +/+ "birdsong") }
  imagePath { ^(score.imagePath +/+ "birdsong") }
}