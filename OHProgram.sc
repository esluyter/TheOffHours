OHProgram {
  var <year, <month, <day;
  var <score;
  var <clips, <birdClips;
  var <deletedClips;
  var <suntimes;
  var nextNodeID = 1001;

  *new { |score, year, month, day|
    ^super.newCopyArgs(year, month, day, score).init;
  }

  init {
    var thisClip, thisClips, thisClipsInclusive;
    var clipsPile = score.clips.scramble.collect(_.program_(this));

    var today = Date.getDate;
    year ?? { year = today.year };
    month ?? { month = today.month };
    day ?? { day = today.day };

    clips = score.cycles.collect({ |cycle| cycle.clips[0].copy.startTime_(54000.0).program_(this) });
    deletedClips = [];

    suntimes = OHSunTimes(year, month, day);

    // filter clips here:
    while ({ clipsPile.size > 0 }) {
      thisClip = clipsPile.removeAt(0);
      thisClips = [];

      if (thisClip.class == OHClip) { //// whyyyyyyyyy

        var conditionalAddClip;

        var currentContinuum = suntimes.continuum(thisClip.startTime);
        var modContinuum = currentContinuum.lincurve(0.1, 0.75, 1.0, 0.0, 2) + currentContinuum.linlin(0.75, 0.92, 0.0, 1.0);
        var breathModContinuum = currentContinuum.linlin(0.2, 0.65, 1.0, 0.0) + currentContinuum.linlin(0.75, 0.92, 0.0, 1.0);

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
            clips = clips.add(thisClip);
          } {
            if (modContinuum.coin) { // only filter if we're in the middle of the night
              clips = clips.add(thisClip);
            } {
              deletedClips = deletedClips.add(thisClip);
            };
          };
        };

        switch (thisClip.type)
        { \grandChord } { clips = clips.add(thisClip) }
        { \shadowChord } { clips = clips.add(thisClip) }
        { \fastBreaths } { thisClip.db = breathModContinuum.linlin(0, 1, -16, 0); conditionalAddClip.() }
        { \slowBreaths } { thisClip.db = breathModContinuum.linlin(0, 1, -16, 0); conditionalAddClip.() }
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
      var density = 0.38;
      // pick a potential birdsong by weight
      clip = score.birdsong.randomBird;
      clipStartTime = startTime - clip.duration;
      if ((clipStartTime >= suntimes.sunsetSeconds) and: (clipStartTime <= OffHoursScore.timeOfDay2Seconds([23, 00]))) {
        density = 0.45;
      };
      clip = clip.copy;
      clip.startTime = clipStartTime;
      if (density.coin) { // <----- change this for different density
        birdClips = birdClips.add(clip);
      };
    };
  }

  duration { ^(score.duration + 20) }
  sampleRate { ^score.sampleRate }

  modeIndexAtSeconds { |seconds|
    ^((suntimes.continuum(seconds).clip(0, 1) * 7) % 6).asInteger;
  }

  dawnChorusStartTime { ^(suntimes.sunriseSeconds - (30 * 60)).clip(0, score.duration); }
  dawnChorusEndTime { ^(suntimes.sunriseSeconds + (15 * 60)).clip(0, score.duration); }
  dawnChorusDuration { ^(this.dawnChorusEndTime - this.dawnChorusStartTime); }

  duskChorusStartTime { ^(suntimes.sunsetSeconds - (30 * 60)).clip(0, score.duration); }
  duskChorusEndTime { ^(suntimes.sunsetSeconds + (15 * 60)).clip(0, score.duration); }
  duskChorusDuration { ^(this.duskChorusEndTime - this.duskChorusStartTime); }

  renderScore { |filename = "test.wav", recordDuration = 54020, action|
    var server = Server(\nrt,
      options: ServerOptions()
      .sampleRate_(48000)
      .numOutputBusChannels_(4)
      .numInputBusChannels_(0)
      .memSize_(1024*1024));

    var score = Score([
      [0.0, ['/d_recv',
        SynthDef(\stream, { |gate = 1|
          var buf = \buf.kr(0);
          var sig = VDiskIn.ar(2, buf, 1);
          var amp = \amp.kr(1);
          var out = \out.kr(0);
          var pan = (LFDNoise3.ar(0.2).exprange(0.01, 1.0) * Line.ar(0, 1, 5)).linlin(0, 1, -1, 1);
          var pannedClarinet = Pan2.ar(sig[1], pan);
          var left = sig[0] + pannedClarinet[0];
          var right = pannedClarinet[1];

          FreeSelf.kr(0.9 - gate);

          Out.ar(out, left * amp);
          Out.ar((out + 1) % 4, right * amp);
        }).asBytes;
      ]],
      [0.0, ['/d_recv',
        SynthDef(\streamVerb, { |gate = 1|
          var buf = \buf.kr(0);
          var sig = VDiskIn.ar(2, buf, 1) * gate;
          var amp = \amp.kr(1);
          var verbAmp = \verbAmp.kr(0);
          var out = \out.kr(0);
          var pan = (LFDNoise3.ar(0.2).exprange(0.01, 1.0) * Line.ar(0, 1, 5)).linlin(0, 1, -1, 1);
          var pannedClarinet = Pan2.ar(sig[1], pan);
          var left = sig[0] + pannedClarinet[0];
          var right = pannedClarinet[1];
          var leftVerb = PartConv.ar(left * 0.03 * amp, 4096, 4);
          var rightVerb = PartConv.ar(right * 0.03 * amp, 4096, 5);

          DetectSilence.ar(leftVerb + gate, 0.0001, doneAction: 2);

          Out.ar(out, (left * amp) + (leftVerb * verbAmp));
          Out.ar((out + 1) % 4, (right * amp) + (rightVerb * verbAmp) + (leftVerb * -9.dbamp));
          Out.ar((out + 2) % 4, (leftVerb * -12.dbamp));
          Out.ar((out + 3) % 4, (leftVerb * -9.dbamp));
        }).asBytes;
      ]],
      [0.0, ['/d_recv',
        SynthDef(\bird, {
          var buf = \buf.kr(0);
          var sig = PlayBuf.ar(2, buf, BufRateScale.kr(buf), doneAction: 2);
          var amp = \amp.kr(1);
          4.do { |i|
            var leftAmp = LFDNoise3.ar(0.2).clip(0, 1);
            var rightAmp = LFDNoise3.ar(0.2).clip(0, 1);
            Out.ar(i, sig[0] * amp * leftAmp);
            Out.ar(i, sig[1] * amp * rightAmp);
          };
        }).asBytes;
      ]],
      [0.0, ['/d_recv',
        SynthDef(\chorus, {
          var out = 0;
          var buf = \buf.kr(0);
          var insig = PlayBuf.ar(1, buf, BufRateScale.kr(buf), startPos: 0, doneAction: 2);
          var verb = PartConv.ar(insig * 0.01, 4096, 4, 1.0);
          var mix = LFDNoise3.kr(LFDNoise3.kr(0.2).exprange(0.1, 0.5));
          var sig = XFade2.ar(insig, verb, mix.linlin(-1, 1, -1, 0)) * mix.linexp(-1, 1, 0.4, 1.0);
          var pan = LFDNoise3.kr(LFDNoise3.kr(0.2).exprange(0.1, 0.5));
          var outsig = PanAz.ar(4, sig, pan) * 3.dbamp;
          Out.ar(out, outsig);
        }).asBytes;
      ]],
      [0.0, ['/d_recv',
        SynthDef(\rotate, {
          var in = In.ar(0, 4);
          var amt = Env([0, 0, 1, 0, 0], [10, 30 * 60 - 10, 15 * 60 - 10, 10], [0, -1, 1, 0] * 0).ar(2);
          var sig = DC.ar(0);
          var offset = LFDNoise3.ar(LFDNoise3.ar(0.2).exprange(0.1, 1)) * LFDNoise3.ar(0.2).exprange(1, 4);
          var lfsawAmt = Env([0, 1, 1, 1, 0], [10, 30 * 60 - 10, 15 * 60 - 10, 10]).ar;
          offset = offset * amt + (LFSaw.ar(0.05 * amt) * lfsawAmt);
          4.do { |i|
            var chanIn = in[i];
            sig = sig + PanAz.ar(4, chanIn, (i.linlin(0, 4, -1, 1) + 0.5 + (offset)).wrap(-1, 1), width: 1.5, orientation: 1);
          };
          ReplaceOut.ar(0, sig);
        }).asBytes;
      ]],
      [0.0, [\b_alloc, 0, 32768, 2]],
      [0.0, [\b_alloc, 1, 32768, 2]],
      [0.0, [\b_alloc, 2, 32768, 2]],
      [0.0, [\b_alloc, 3, 32768, 2]],
      [0.0, ["/b_allocRead", 4, this.score.path +/+ "samples/ir/irspectrumLf.wav", 0, -1]],
      [0.0, ["/b_allocRead", 5, this.score.path +/+ "samples/ir/irspectrumRf.wav", 0, -1]]
    ]);

    this.score.birdsong.addBuffersToScore(score);

    // dusk / dawn
    score.add([this.duskChorusStartTime, [\s_new, \rotate, 999, 1, 1]]);
    [ // chorus members
      \AE,
      \CG,
      \DA,
      \EB_High,
      \EB_Low,
      \FC_High,
      \FC_Low,
      \GD,
      \birds0,
      \birds1,
      \birds2
    ].do { |name, i|
      score.add([0.0, ["/b_allocRead", 50 + i, this.score.path +/+ "samples/chorus/" ++ name ++ ".wav", 0, -1]]);
      score.add([this.duskChorusStartTime, (Synth.basicNew(\chorus, server, 50 + i)).newMsg(args: [buf: 50 + i])])
    };

    clips.do { |clip|
      clip.addToScore(score, server);
    };

    birdClips.do { |clip|
      clip.addBirdToScore(score, server);
    };

    score.recordNRT(
      outputFilePath: (this.score.recordPath +/+ filename).resolveRelative.debug("recording to"),
      headerFormat: "w64",
      sampleFormat: "int16",
      options: server.options,
      duration: recordDuration, //15 * 60 * 60,
      action: {
        ("ffmpeg -i " ++ this.score.recordPath +/+ filename ++ " -rf64 auto -ss 36000 " ++ this.score.recordPath +/+ "trim-" ++ filename).unixCmd; // trim from 10 hours till end
        action.value
      }
    );


    server.remove;

    ^score;
  }
}