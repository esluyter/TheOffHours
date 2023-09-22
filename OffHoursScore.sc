OffHoursScore {
  var <path;
  var <cycles, <birdsong;
  var <sampleRate = 48000;
  var <duration = 54000.0; // 15 * 60 * 60

  *new { |path, arr|
    ^super.newCopyArgs(path).init(arr);
  }

  init { |arr|
    var durations = [18.75 * 60, 30.0 * 60, 25.0 * 60, 20.0 * 60];
    // parse array into cycles / clips
    cycles = { |i| OHCycle(this, i, durations[i], arr[i]) } ! 4;
    birdsong = OHBirdsong(this, arr[4]);
  }

  clipsAtTime { |seconds, modeIndex = 0| ^cycles.collect(_.clipAtTime(seconds, modeIndex)); }

  clips {
    ^cycles.collect(_.clips(duration)).flat.sort({ |a, b| b.startTime > a.startTime });
  }

  modeIndexAtTime { |seconds| // here is where to control mode vis a vis elapsed time (since 4:30 pm)
    ^((seconds / duration).lincurve(0, 1, 0, 7, 1)).asInteger % 6;
  }

  generateProgram { |year, month, day|
    if (year.isNil) {
      var today = Date.getDate;
      year = today.year;
      month = today.month;
      day = today.day;
    };

    ^OHProgram(this, year, month, day);
  }

  generateImages {
    // this may take a while
    cycles.do(_.generateImages);
  }

  generateBirdImages {
    birdsong.generateImages;
  }

  audioPath { ^(path +/+ "samples") }
  imagePath { ^(path +/+ "spectrograms") }
}
