OffHoursScore {
  var <path;
  var <cycles, <birdsong;
  var <sampleRate = 48000; // everything needs to run at 48, or the whole thing breaks?
  var <duration = 54000.0; // 15 hours, in seconds
  classvar <startHour = 16;
  classvar <startMinute = 30;

  *seconds2TimeOfDay { |seconds|
    var rawMinutes = seconds / 60;
    var rawHour = ((rawMinutes + startMinute) / 60).floor;
    var hour = (rawHour + startHour).asInteger % 24;
    var minutes = ((rawMinutes + 30) % 60);
    ^[hour, minutes];
  }

  *timeOfDay2Seconds { |arr|
    var rawHour, rawMinutes;
    var hour, minutes;
    #hour, minutes = arr;
    rawHour = (hour - startHour) % 24;
    rawMinutes = (minutes - startMinute);
    ^((rawHour * 60 + rawMinutes) * 60);
  }

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

  generateProgram { |year, month, day|
    if (year.isNil) {
      var today = Date.getDate;
      year = today.year;
      month = today.month;
      day = today.day;
    };

    ^OHProgram(this, year, month, day);
  }

  generateImages { |skipClips = 0, skipCycles = 0|
    // this may take a while
    cycles[skipCycles..].collect(_.clips).flat[skipClips..].do(_.generateImages);
  }

  generateBirdImages {
    birdsong.generateImages;
  }

  recordPath { ^(path +/+ "render") }
  audioPath { ^(path +/+ "samples") }
  imagePath { ^(path +/+ "spectrograms") }
}
