OHCycle {
  var <score, <speaker, <duration;
  var clips;

  *new { |score, speaker, duration, arr|
    ^super.newCopyArgs(score, speaker, duration).init(arr);
  }

  init { |arr|
    clips = [];
    arr.pairsDo { |range, type, i|
      var startTime = range.key;
      var duration = range.value - startTime;
      clips = clips.add(OHClip(this, type, startTime, duration, i));
    };
  }

  clipAtTime { |seconds|
    var ret = nil;
    seconds = seconds % duration;
    clips.do { |clip|
      if ((clip.startTime <= seconds) and: (clip.endTime >= seconds)) {
        ret = clip;
      };
    };
    ^ret;
  }

  clips { |argduration|
    var nRepeats;
    var ret = [];
    argduration ?? { argduration = duration };
    nRepeats = (argduration / duration).asInteger;
    nRepeats.do { |i|
      clips.do { |clip|
        ret = ret.add(clip.copy.startTime_(clip.startTime + (i * duration)));
      };
    };
    ^ret;
  }

  audioPath { ^score.audioPath }
  imagePath { ^score.imagePath }
}