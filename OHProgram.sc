OHProgram {
  var <year, <month, <day;
  var <score;
  var <clips, <birdClips;
  var <deletedClips;
  var nextNodeID = 1001;

  *new { |score, year, month, day|
    ^super.newCopyArgs(year, month, day, score).init;
  }

  init {
    var thisClip, thisClips, thisClipsInclusive;
    var clipsPile = score.clips.scramble;
    clips = [];
    deletedClips = [];

    // filter clips here:
    while ({ clipsPile.size > 0 }) {
      thisClip = clipsPile.removeAt(0);
      thisClips = [];

      if (thisClip.class == OHClip) { //// whyyyyyyyyy

        var conditionalAddClip;

        var inclusive = true;//thisClip.type == \calls;
        var typeSensitive = true;

        if (inclusive) {
          thisClips = clips.select({ |clip| ((clip.startTime <= thisClip.endTime) and: (clip.endTime >= thisClip.startTime)) });
        } {
          thisClips = clips.select({ |clip| ((clip.startTime <= thisClip.startTime) and: (clip.endTime >= thisClip.startTime)) });
        };


        if (typeSensitive) {
          thisClips = thisClips.select({ |clip| clip.type == thisClip.type });
        };

        conditionalAddClip = {
          if (thisClips.size == 0) {
            clips = clips.add(thisClip)
          } {
            deletedClips = deletedClips.add(thisClip)
          }
        };

        switch (thisClip.type)
        { \grandChord } { clips = clips.add(thisClip) }
        { \shadowChord } { clips = clips.add(thisClip) }
        { \fastBreaths } { conditionalAddClip.() }
        { \slowBreaths } { conditionalAddClip.() }
        { \calls } { conditionalAddClip.() }
        { deletedClips = deletedClips.add(thisClip) };

      }; /// end whyyyyyy
    };

    this.initBirdClips((year.asString ++ month.asString.padLeft(2, "0") ++ day.asString.padLeft(2, "0")).asInteger);
  }

  initBirdClips { |seed = 0|
    birdClips = [];
    thisThread.randSeed = seed;

    // 120 grand chord events in 15 hours
    clips
    .select({ |clip| clip.type == \grandChord })
    .collect(_.startTime)
    .asSet
    .asArray
    .sort
    [1..] // leave out the first one because birdsong can't happen in negative time
    .do { |startTime|
      var clip, clipStartTime;
      // pick a potential birdsong by weight
      clip = score.birdsong.randomBird;
      clipStartTime = startTime - clip.duration;
      clip = clip.copy;
      clip.startTime = clipStartTime;
      if (0.38.coin) { // <----- change this for different density
        birdClips = birdClips.add(clip);
      };
    };
  }

  duration { ^score.duration }
  sampleRate { ^score.sampleRate }

  renderScore {
    var server = Server(\nrt,
      options: ServerOptions()
      .sampleRate_(48000)
      .numOutputBusChannels_(4)
      .numInputBusChannels_(0));

    var score = Score([
      [0.0, ['/d_recv',
        SynthDef(\stream, {
          var buf = \buf.kr(0);
          var sig = VDiskIn.ar(1, buf, 1);
          var amp = \amp.kr(0.1);
          var out = \out.kr(0);
          Out.ar(out, sig * amp);
        }).asBytes;
      ]],
      [0.0, [\b_alloc, 0, 32768, 1]],
      [0.0, [\b_alloc, 1, 32768, 1]],
      [0.0, [\b_alloc, 2, 32768, 1]],
      [0.0, [\b_alloc, 3, 32768, 1]],
    ]);

    clips.do { |clip|
      clip.addToScore(score, server);
    };

    birdClips.do { |clip|
      clip.addToScore(score, server);
    };


    score.recordNRT(
      outputFilePath: "/Users/ericsluyter/Documents/sc temp/Off Hours/render/test.wav",
      headerFormat: "w64",
      sampleFormat: "int16",
      options: server.options,
      duration: 15 * 60 * 60,
      action: { "done".postln }
    );


    server.remove;

    ^score;
  }
}