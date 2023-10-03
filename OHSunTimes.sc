OHSunTimes {
  var <sunrise, <sunset;

  sunriseSeconds { ^OffHoursScore.timeOfDay2Seconds(sunrise); }
  sunsetSeconds { ^OffHoursScore.timeOfDay2Seconds(sunset); }

  continuum { |seconds| ^seconds.linlin(this.sunsetSeconds - 6000, this.sunriseSeconds, 0, 1, nil) }

  *new { |year, month, day|
    var strtimes = [];
    var months = [\Jan, \Feb, \Mar, \Apr, \May, \Jun, \Jul, \Aug, \Sep, \Oct, \Nov, \Dec];
    var today = Date.getDate;
    year ?? { year = today.year };
    month ?? { month = today.month };
    day ?? { day = today.day };

    //var suntimes = ();

    //https://in-the-sky.org/sunrise.php?startday=15&startmonth=4&startyear=2024&interval=4&tz=0

    strtimes = strtimes.add("2023	Sep	04	19 days	06:55	13:39	20:21	Leo	22:02	05:05	12:46	Aries
2023	Sep	05	20 days	06:57	13:38	20:18	Leo	22:24	05:55	14:09	Aries
2023	Sep	06	21 days	06:58	13:38	20:16	Leo	22:53	06:47	15:28	Taurus
2023	Sep	07	22 days	07:00	13:38	20:14	Leo	23:32	07:41	16:40	Taurus
2023	Sep	08	23 days	07:02	13:37	20:12	Leo	--:--	08:34	17:39	Taurus
2023	Sep	09	24 days	07:03	13:37	20:10	Leo	00:24	09:27	18:24	Gemini
2023	Sep	10	25 days	07:05	13:37	20:07	Leo	01:26	10:18	18:57	Gemini
2023	Sep	11	26 days	07:06	13:36	20:05	Leo	02:35	11:07	19:21	Cancer
2023	Sep	12	27 days	07:08	13:36	20:03	Leo	03:47	11:52	19:39	Cancer
2023	Sep	13	28 days	07:09	13:35	20:01	Leo	04:59	12:35	19:54	Leo
2023	Sep	14	29 days	07:11	13:35	19:58	Leo	06:10	13:16	20:06	Leo
2023	Sep	15	30 days	07:12	13:35	19:56	Leo	07:19	13:56	20:16	Leo
2023	Sep	16	01 days	07:14	13:34	19:54	Leo	08:29	14:35	20:27	Virgo
2023	Sep	17	02 days	07:16	13:34	19:52	Leo	09:40	15:16	20:38	Virgo
2023	Sep	18	03 days	07:17	13:34	19:49	Virgo	10:53	15:58	20:51	Virgo
2023	Sep	19	04 days	07:19	13:33	19:47	Virgo	12:10	16:44	21:08	Libra
2023	Sep	20	05 days	07:20	13:33	19:45	Virgo	13:28	17:34	21:30	Libra
2023	Sep	21	06 days	07:22	13:33	19:42	Virgo	14:48	18:28	22:02	Scorpius
2023	Sep	22	07 days	07:23	13:32	19:40	Virgo	16:02	19:26	22:49	Ophiuchus
2023	Sep	23	08 days	07:25	13:32	19:38	Virgo	17:03	20:27	23:54	Sagittarius
2023	Sep	24	09 days	07:27	13:32	19:36	Virgo	17:50	21:29	--:--	Sagittarius
2023	Sep	25	10 days	07:28	13:31	19:33	Virgo	18:23	22:28	01:15	Capricornus
2023	Sep	26	11 days	07:30	13:31	19:31	Virgo	18:46	23:25	02:46	Capricornus
2023	Sep	27	12 days	07:31	13:31	19:29	Virgo	19:04	--:--	04:18	Aquarius
2023	Sep	28	13 days	07:33	13:30	19:27	Virgo	19:19	00:19	05:50	Aquarius
2023	Sep	29	14 days	07:34	13:30	19:24	Virgo	19:33	01:11	07:20	Pisces
2023	Sep	30	15 days	07:36	13:30	19:22	Virgo	19:48	02:01	08:48	Pisces
2023	Oct	01	16 days	07:38	13:29	19:20	Virgo	20:04	02:51	10:16	Aries
2023	Oct	02	17 days	07:39	13:29	19:18	Virgo	20:24	03:43	11:43	Aries
2023	Oct	03	18 days	07:41	13:29	19:15	Virgo	20:50	04:36	13:07	Taurus
2023	Oct	04	19 days	07:42	13:28	19:13	Virgo	21:26	05:30	14:25	Taurus
2023	Oct	05	20 days	07:44	13:28	19:11	Virgo	22:14	06:25	15:31	Taurus
2023	Oct	06	21 days	07:46	13:28	19:09	Virgo	23:13	07:20	16:22	Auriga
2023	Oct	07	22 days	07:47	13:27	19:07	Virgo	--:--	08:12	17:00	Gemini
2023	Oct	08	23 days	07:49	13:27	19:04	Virgo	00:21	09:02	17:27	Cancer
2023	Oct	09	24 days	07:50	13:27	19:02	Virgo	01:33	09:49	17:47	Cancer
2023	Oct	10	25 days	07:52	13:27	19:00	Virgo	02:46	10:32	18:02	Leo
2023	Oct	11	26 days	07:54	13:26	18:58	Virgo	03:57	11:14	18:14	Leo
2023	Oct	12	27 days	07:55	13:26	18:56	Virgo	05:07	11:54	18:25	Leo
2023	Oct	13	28 days	07:57	13:26	18:54	Virgo	06:17	12:34	18:36	Virgo
2023	Oct	14	29 days	07:59	13:26	18:51	Virgo	07:28	13:14	18:47	Virgo
2023	Oct	15	01 days	08:00	13:25	18:49	Virgo	08:42	13:57	18:59	Virgo
2023	Oct	16	02 days	08:02	13:25	18:47	Virgo	09:58	14:42	19:14	Virgo
2023	Oct	17	03 days	08:04	13:25	18:45	Virgo	11:17	15:31	19:35	Libra
2023	Oct	18	04 days	08:05	13:25	18:43	Virgo	12:37	16:24	20:03	Scorpius
2023	Oct	19	05 days	08:07	13:24	18:41	Virgo	13:53	17:21	20:45	Ophiuchus
2023	Oct	20	06 days	08:09	13:24	18:39	Virgo	14:59	18:20	21:43	Sagittarius
2023	Oct	21	07 days	08:10	13:24	18:37	Virgo	15:49	19:21	22:58	Sagittarius
2023	Oct	22	08 days	08:12	13:24	18:35	Virgo	16:25	20:19	--:--	Sagittarius
2023	Oct	23	09 days	08:14	13:24	18:33	Virgo	16:50	21:15	00:23	Capricornus
2023	Oct	24	10 days	08:16	13:24	18:31	Virgo	17:09	22:08	01:53	Capricornus
2023	Oct	25	11 days	08:17	13:24	18:29	Virgo	17:25	22:59	03:22	Aquarius
2023	Oct	26	12 days	08:19	13:23	18:27	Virgo	17:38	23:48	04:49	Aquarius
2023	Oct	27	13 days	08:21	13:23	18:25	Virgo	17:52	--:--	06:16	Cetus
2023	Oct	28	14 days	08:22	13:23	18:23	Virgo	18:07	00:38	07:43	Pisces
2023	Oct	29	15 days	07:24	12:23	17:21	Virgo	17:25	01:28	08:10	Aries
2023	Oct	30	16 days	07:26	12:23	17:20	Virgo	17:48	01:21	09:37	Aries
2023	Oct	31	17 days	07:28	12:23	17:18	Virgo	18:20	02:15	11:00	Taurus
2023	Nov	01	18 days	07:29	12:23	17:16	Libra	19:03	03:12	12:14	Taurus
2023	Nov	02	19 days	07:31	12:23	17:14	Libra	19:59	04:08	13:14	Auriga
2023	Nov	03	20 days	07:33	12:23	17:12	Libra	21:05	05:03	13:58	Gemini
2023	Nov	04	21 days	07:34	12:23	17:11	Libra	22:17	05:55	14:29	Gemini
2023	Nov	05	22 days	07:36	12:23	17:09	Libra	23:30	06:43	14:52	Cancer
2023	Nov	06	23 days	07:38	12:23	17:07	Libra	--:--	07:28	15:08	Leo
2023	Nov	07	24 days	07:40	12:23	17:06	Libra	00:42	08:10	15:22	Leo
2023	Nov	08	25 days	07:41	12:23	17:04	Libra	01:52	08:51	15:33	Leo
2023	Nov	09	26 days	07:43	12:23	17:03	Libra	03:02	09:30	15:44	Virgo
2023	Nov	10	27 days	07:45	12:23	17:01	Libra	04:12	10:10	15:54	Virgo
2023	Nov	11	28 days	07:46	12:23	17:00	Libra	05:25	10:52	16:06	Virgo
2023	Nov	12	29 days	07:48	12:23	16:58	Libra	06:40	11:36	16:21	Virgo
2023	Nov	13	30 days	07:50	12:24	16:57	Libra	07:59	12:25	16:39	Libra
2023	Nov	14	01 days	07:52	12:24	16:55	Libra	09:21	13:17	17:05	Libra
2023	Nov	15	02 days	07:53	12:24	16:54	Libra	10:41	14:14	17:43	Scorpius
2023	Nov	16	03 days	07:55	12:24	16:53	Libra	11:52	15:14	18:36	Ophiuchus
2023	Nov	17	04 days	07:57	12:24	16:51	Libra	12:47	16:15	19:47	Sagittarius
2023	Nov	17	04 days	07:57	12:24	16:51	Libra	12:47	16:15	19:47	Sagittarius
2023	Nov	18	05 days	07:58	12:24	16:50	Libra	13:27	17:15	21:10	Sagittarius
2023	Nov	19	06 days	08:00	12:25	16:49	Libra	13:55	18:11	22:38	Capricornus
2023	Nov	20	07 days	08:02	12:25	16:48	Libra	14:16	19:04	--:--	Capricornus
2023	Nov	21	08 days	08:03	12:25	16:46	Libra	14:32	19:53	00:06	Aquarius
2023	Nov	22	09 days	08:05	12:25	16:45	Libra	14:45	20:41	01:31	Aquarius
2023	Nov	23	10 days	08:06	12:26	16:44	Libra	14:58	21:29	02:55	Pisces
2023	Nov	24	11 days	08:08	12:26	16:43	Scorpius	15:12	22:18	04:19	Pisces
2023	Nov	25	12 days	08:09	12:26	16:42	Scorpius	15:28	23:08	05:43	Aries
2023	Nov	26	13 days	08:11	12:26	16:42	Scorpius	15:49	--:--	07:09	Aries
2023	Nov	27	14 days	08:12	12:27	16:41	Scorpius	16:16	00:01	08:34	Taurus
2023	Nov	28	15 days	08:14	12:27	16:40	Scorpius	16:54	00:57	09:52	Taurus
2023	Nov	29	16 days	08:15	12:27	16:39	Scorpius	17:44	01:54	10:59	Taurus
2023	Nov	30	17 days	08:17	12:28	16:38	Scorpius	18:47	02:50	11:51	Gemini
2023	Dec	01	18 days	08:18	12:28	16:38	Ophiuchus	19:58	03:44	12:28	Gemini
2023	Dec	02	19 days	08:20	12:29	16:37	Ophiuchus	21:12	04:35	12:54	Cancer
2023	Dec	03	20 days	08:21	12:29	16:37	Ophiuchus	22:24	05:21	13:13	Cancer
2023	Dec	04	21 days	08:22	12:29	16:36	Ophiuchus	23:36	06:05	13:28	Leo
2023	Dec	05	22 days	08:24	12:30	16:36	Ophiuchus	--:--	06:46	13:40	Leo
2023	Dec	06	23 days	08:25	12:30	16:35	Ophiuchus	00:45	07:25	13:50	Leo
2023	Dec	07	24 days	08:26	12:31	16:35	Ophiuchus	01:55	08:05	14:01	Virgo
2023	Dec	08	25 days	08:27	12:31	16:35	Ophiuchus	03:05	08:45	14:12	Virgo
2023	Dec	09	26 days	08:28	12:31	16:34	Ophiuchus	04:18	09:28	14:25	Virgo
2023	Dec	10	27 days	08:29	12:32	16:34	Ophiuchus	05:36	10:14	14:42	Libra
2023	Dec	11	28 days	08:30	12:32	16:34	Ophiuchus	06:57	11:05	15:05	Libra
2023	Dec	12	29 days	08:31	12:33	16:34	Ophiuchus	08:19	12:01	15:37	Scorpius
2023	Dec	13	01 days	08:32	12:33	16:34	Ophiuchus	09:36	13:02	16:26	Ophiuchus
2023	Dec	14	02 days	08:33	12:34	16:34	Ophiuchus	10:40	14:04	17:32	Sagittarius
2023	Dec	15	03 days	08:34	12:34	16:34	Ophiuchus	11:26	15:06	18:54	Sagittarius
2023	Dec	16	04 days	08:35	12:35	16:34	Ophiuchus	11:58	16:05	20:23	Capricornus
2023	Dec	17	05 days	08:36	12:35	16:34	Ophiuchus	12:21	17:00	21:52	Capricornus
2023	Dec	18	06 days	08:37	12:36	16:35	Ophiuchus	12:38	17:51	23:19	Aquarius
2023	Dec	19	07 days	08:37	12:36	16:35	Sagittarius	12:53	18:39	--:--	Aquarius
2023	Dec	20	08 days	08:38	12:37	16:35	Sagittarius	13:06	19:26	00:43	Pisces
2023	Dec	21	09 days	08:38	12:37	16:36	Sagittarius	13:19	20:13	02:05	Pisces
2023	Dec	22	10 days	08:39	12:38	16:36	Sagittarius	13:34	21:02	03:28	Pisces
2023	Dec	23	11 days	08:39	12:38	16:37	Sagittarius	13:52	21:53	04:51	Aries
2023	Dec	24	12 days	08:40	12:39	16:37	Sagittarius	14:16	22:46	06:14	Aries
2023	Dec	25	13 days	08:40	12:39	16:38	Sagittarius	14:49	23:42	07:34	Taurus
2023	Dec	26	14 days	08:41	12:40	16:39	Sagittarius	15:34	--:--	08:45	Taurus
2023	Dec	27	15 days	08:41	12:40	16:39	Sagittarius	16:32	00:39	09:42	Auriga");
    // must break here because too long for one string ughh

    strtimes = strtimes.add("2023	Dec	28	16 days	08:41	12:41	16:40	Sagittarius	17:40	01:34	10:25	Gemini
2023	Dec	29	17 days	08:41	12:41	16:41	Sagittarius	18:54	02:26	10:55	Cancer
2023	Dec	30	18 days	08:41	12:42	16:42	Sagittarius	20:08	03:14	11:16	Cancer
2023	Dec	31	19 days	08:41	12:42	16:43	Sagittarius	21:20	03:59	11:33	Leo
2024	Jan	01	20 days	08:41	12:43	16:44	Sagittarius	22:30	04:41	11:46	Leo
2024	Jan	02	21 days	08:41	12:43	16:45	Sagittarius	23:39	05:21	11:57	Leo
2024	Jan	03	22 days	08:41	12:43	16:46	Sagittarius	--:--	06:00	12:07	Virgo
2024	Jan	04	23 days	08:41	12:44	16:47	Sagittarius	00:48	06:39	12:18	Virgo
2024	Jan	05	24 days	08:41	12:44	16:48	Sagittarius	01:59	07:20	12:30	Virgo
2024	Jan	06	25 days	08:41	12:45	16:49	Sagittarius	03:12	08:04	12:44	Virgo
2024	Jan	07	26 days	08:40	12:45	16:51	Sagittarius	04:30	08:52	13:03	Libra
2024	Jan	08	27 days	08:40	12:46	16:52	Sagittarius	05:52	09:45	13:31	Scorpius
2024	Jan	09	28 days	08:39	12:46	16:53	Sagittarius	07:12	10:43	14:11	Scorpius
2024	Jan	10	29 days	08:39	12:47	16:55	Sagittarius	08:23	11:46	15:09	Sagittarius
2024	Jan	11	30 days	08:38	12:47	16:56	Sagittarius	09:18	12:50	16:27	Sagittarius
2024	Jan	12	01 days	08:38	12:47	16:57	Sagittarius	09:57	13:52	17:57	Sagittarius
2024	Jan	13	02 days	08:37	12:48	16:59	Sagittarius	10:24	14:50	19:30	Capricornus
2024	Jan	14	03 days	08:36	12:48	17:00	Sagittarius	10:43	15:45	21:01	Aquarius
2024	Jan	15	04 days	08:36	12:48	17:02	Sagittarius	10:59	16:35	22:28	Aquarius
2024	Jan	16	05 days	08:35	12:49	17:03	Sagittarius	11:13	17:24	23:53	Pisces
2024	Jan	17	06 days	08:34	12:49	17:05	Sagittarius	11:26	18:11	--:--	Pisces
2024	Jan	18	07 days	08:33	12:49	17:06	Sagittarius	11:40	19:00	01:17	Pisces
2024	Jan	19	08 days	08:32	12:50	17:08	Sagittarius	11:57	19:49	02:40	Aries
2024	Jan	20	09 days	08:31	12:50	17:10	Sagittarius	12:19	20:42	04:02	Aries
2024	Jan	21	10 days	08:30	12:50	17:11	Capricornus	12:48	21:36	05:22	Taurus
2024	Jan	22	11 days	08:29	12:51	17:13	Capricornus	13:28	22:31	06:36	Taurus
2024	Jan	23	12 days	08:28	12:51	17:14	Capricornus	14:22	23:26	07:37	Auriga
2024	Jan	24	13 days	08:27	12:51	17:16	Capricornus	15:27	--:--	08:23	Gemini
2024	Jan	25	14 days	08:26	12:51	17:18	Capricornus	16:39	00:19	08:57	Gemini
2024	Jan	26	15 days	08:24	12:52	17:20	Capricornus	17:53	01:09	09:21	Cancer
2024	Jan	27	16 days	08:23	12:52	17:21	Capricornus	19:06	01:55	09:38	Leo
2024	Jan	28	17 days	08:22	12:52	17:23	Capricornus	20:16	02:38	09:52	Leo
2024	Jan	29	18 days	08:20	12:52	17:25	Capricornus	21:26	03:18	10:04	Leo
2024	Jan	30	19 days	08:19	12:52	17:26	Capricornus	22:34	03:57	10:14	Virgo
2024	Jan	31	20 days	08:18	12:53	17:28	Capricornus	23:44	04:36	10:24	Virgo
2024	Feb	01	21 days	08:16	12:53	17:30	Capricornus	--:--	05:16	10:35	Virgo
2024	Feb	02	22 days	08:15	12:53	17:32	Capricornus	00:55	05:57	10:48	Virgo
2024	Feb	03	23 days	08:13	12:53	17:34	Capricornus	02:10	06:42	11:04	Libra
2024	Feb	04	24 days	08:12	12:53	17:35	Capricornus	03:28	07:31	11:27	Libra
2024	Feb	05	25 days	08:10	12:53	17:37	Capricornus	04:47	08:26	11:59	Scorpius
2024	Feb	06	26 days	08:08	12:53	17:39	Capricornus	06:01	09:25	12:47	Ophiuchus
2024	Feb	07	27 days	08:07	12:53	17:41	Capricornus	07:04	10:28	13:55	Sagittarius
2024	Feb	08	28 days	08:05	12:53	17:42	Capricornus	07:50	11:31	15:20	Sagittarius
2024	Feb	09	29 days	08:03	12:53	17:44	Capricornus	08:22	12:32	16:53	Capricornus
2024	Feb	10	01 days	08:02	12:54	17:46	Capricornus	08:45	13:30	18:28	Capricornus
2024	Feb	11	02 days	08:00	12:54	17:48	Capricornus	09:03	14:23	20:01	Aquarius
2024	Feb	12	03 days	07:58	12:54	17:50	Capricornus	09:18	15:15	21:30	Aquarius
2024	Feb	13	04 days	07:56	12:54	17:51	Capricornus	09:31	16:05	22:58	Pisces
2024	Feb	14	05 days	07:55	12:54	17:53	Capricornus	09:45	16:54	--:--	Pisces
2024	Feb	15	06 days	07:53	12:53	17:55	Capricornus	10:02	17:45	00:24	Aries
2024	Feb	16	07 days	07:51	12:53	17:57	Capricornus	10:22	18:37	01:50	Aries
2024	Feb	17	08 days	07:49	12:53	17:59	Capricornus	10:49	19:32	03:12	Taurus
2024	Feb	18	09 days	07:47	12:53	18:00	Aquarius	11:26	20:27	04:29	Taurus
2024	Feb	19	10 days	07:45	12:53	18:02	Aquarius	12:15	21:22	05:34	Taurus
2024	Feb	20	11 days	07:43	12:53	18:04	Aquarius	13:17	22:15	06:24	Gemini
2024	Feb	21	12 days	07:41	12:53	18:06	Aquarius	14:27	23:05	07:00	Gemini
2024	Feb	22	13 days	07:39	12:53	18:07	Aquarius	15:40	23:52	07:26	Cancer
2024	Feb	23	14 days	07:37	12:53	18:09	Aquarius	16:53	--:--	07:45	Leo
2024	Feb	24	15 days	07:35	12:53	18:11	Aquarius	18:05	00:36	08:00	Leo
2024	Feb	25	16 days	07:33	12:52	18:13	Aquarius	19:15	01:17	08:12	Leo
2024	Feb	26	17 days	07:31	12:52	18:14	Aquarius	20:24	01:57	08:22	Leo
2024	Feb	27	18 days	07:29	12:52	18:16	Aquarius	21:33	02:35	08:32	Virgo
2024	Feb	28	19 days	07:27	12:52	18:18	Aquarius	22:43	03:14	08:43	Virgo
2024	Feb	29	20 days	07:25	12:52	18:20	Aquarius	23:56	03:55	08:54	Virgo
2024	Mar	01	21 days	07:23	12:52	18:21	Aquarius	00:00	04:38	09:09	Libra
2024	Mar	02	22 days	07:21	12:51	18:23	Aquarius	01:12	05:24	09:28	Libra
2024	Mar	03	23 days	07:18	12:51	18:25	Aquarius	02:29	06:15	09:55	Scorpius
2024	Mar	04	24 days	07:16	12:51	18:27	Aquarius	03:44	07:11	10:34	Ophiuchus
2024	Mar	05	25 days	07:14	12:51	18:28	Aquarius	04:50	08:10	11:31	Sagittarius
2024	Mar	06	26 days	07:12	12:51	18:30	Aquarius	05:42	09:11	12:46	Sagittarius
2024	Mar	07	27 days	07:10	12:50	18:32	Aquarius	06:19	10:12	14:14	Capricornus
2024	Mar	08	28 days	07:08	12:50	18:33	Aquarius	06:46	11:10	15:48	Capricornus
2024	Mar	09	29 days	07:05	12:50	18:35	Aquarius	07:05	12:06	17:22	Aquarius
2024	Mar	10	30 days	07:03	12:50	18:37	Aquarius	07:21	12:59	18:55	Aquarius
2024	Mar	11	01 days	07:01	12:49	18:38	Aquarius	07:35	13:51	20:25	Pisces
2024	Mar	12	02 days	06:59	12:49	18:40	Aquarius	07:49	14:42	21:56	Pisces
2024	Mar	13	03 days	06:57	12:49	18:42	Pisces	08:05	15:34	23:25	Aries
2024	Mar	14	04 days	06:54	12:48	18:43	Pisces	08:24	16:28	--:--	Aries
2024	Mar	15	05 days	06:52	12:48	18:45	Pisces	08:48	17:23	00:54	Taurus
2024	Mar	16	06 days	06:50	12:48	18:47	Pisces	09:22	18:20	02:16	Taurus
2024	Mar	17	07 days	06:48	12:48	18:48	Pisces	10:08	19:16	03:27	Taurus
2024	Mar	18	08 days	06:46	12:47	18:50	Pisces	11:07	20:11	04:23	Auriga
2024	Mar	19	09 days	06:43	12:47	18:52	Pisces	12:15	21:02	05:04	Gemini
2024	Mar	20	10 days	06:41	12:47	18:53	Pisces	13:28	21:50	05:32	Cancer
2024	Mar	21	11 days	06:39	12:46	18:55	Pisces	14:42	22:34	05:53	Cancer
2024	Mar	22	12 days	06:37	12:46	18:57	Pisces	15:54	23:16	06:08	Leo
2024	Mar	23	13 days	06:34	12:46	18:58	Pisces	17:04	23:56	06:21	Leo
2024	Mar	24	14 days	06:32	12:46	19:00	Pisces	18:13	--:--	06:31	Leo
2024	Mar	25	15 days	06:30	12:45	19:02	Pisces	19:23	00:35	06:41	Virgo
2024	Mar	26	16 days	06:28	12:45	19:03	Pisces	20:33	01:14	06:52	Virgo
2024	Mar	27	17 days	06:25	12:45	19:05	Pisces	21:45	01:54	07:03	Virgo
2024	Mar	28	18 days	06:23	12:44	19:07	Pisces	23:01	02:36	07:16	Virgo
2024	Mar	29	19 days	06:21	12:44	19:08	Pisces	--:--	03:22	07:33	Libra
2024	Mar	30	20 days	06:19	12:44	19:10	Pisces	00:18	04:11	07:57	Scorpius
2024	Mar	31	21 days	07:16	13:43	20:12	Pisces	01:33	06:04	09:31	Scorpius
2024	Apr	01	22 days	07:14	13:43	20:13	Pisces	03:41	07:01	10:20	Sagittarius
2024	Apr	02	23 days	07:12	13:43	20:15	Pisces	04:37	08:00	11:26	Sagittarius
2024	Apr	03	24 days	07:10	13:43	20:16	Pisces	05:18	08:59	12:47	Sagittarius
2024	Apr	04	25 days	07:07	13:42	20:18	Pisces	05:47	09:56	14:16	Capricornus
2024	Apr	05	26 days	07:05	13:42	20:20	Pisces	06:09	10:51	15:47	Capricornus
2024	Apr	06	27 days	07:03	13:42	20:21	Pisces	06:25	11:43	17:18	Aquarius");

    strtimes = strtimes.add("2024	Apr	07	28 days	07:01	13:41	20:23	Pisces	06:40	12:35	18:48	Aquarius
2024	Apr	08	29 days	06:59	13:41	20:25	Pisces	06:53	13:26	20:19	Cetus
2024	Apr	09	01 days	06:57	13:41	20:26	Pisces	07:08	14:18	21:50	Pisces
2024	Apr	10	02 days	06:54	13:41	20:28	Pisces	07:25	15:12	23:22	Aries
2024	Apr	11	03 days	06:52	13:40	20:30	Pisces	07:47	16:08	--:--	Aries
2024	Apr	12	04 days	06:50	13:40	20:31	Pisces	08:17	17:07	00:51	Taurus
2024	Apr	13	05 days	06:48	13:40	20:33	Pisces	08:59	18:05	02:10	Taurus
2024	Apr	14	06 days	06:46	13:40	20:34	Pisces	09:54	19:02	03:15	Auriga
2024	Apr	15	07 days	06:44	13:39	20:36	Pisces	11:01	19:56	04:03	Gemini
2024	Apr	16	08 days	06:42	13:39	20:38	Pisces	12:15	20:46	04:36	Cancer
2024	Apr	17	09 days	06:39	13:39	20:39	Pisces	13:29	21:32	04:59	Cancer
2024	Apr	18	10 days	06:37	13:39	20:41	Pisces	14:42	22:14	05:16	Leo
2024	Apr	19	11 days	06:35	13:38	20:43	Aries	15:53	22:55	05:29	Leo
2024	Apr	20	12 days	06:33	13:38	20:44	Aries	17:02	23:34	05:40	Leo
2024	Apr	21	13 days	06:31	13:38	20:46	Aries	18:11	--:--	05:51	Virgo
2024	Apr	22	14 days	06:29	13:38	20:47	Aries	19:21	00:13	06:00	Virgo
2024	Apr	23	15 days	06:27	13:38	20:49	Aries	20:33	00:53	06:11	Virgo
2024	Apr	24	16 days	06:25	13:37	20:51	Aries	21:48	01:35	06:24	Virgo
2024	Apr	25	17 days	06:23	13:37	20:52	Aries	23:05	02:19	06:40	Libra
2024	Apr	26	18 days	06:21	13:37	20:54	Aries	--:--	03:08	07:01	Libra
2024	Apr	27	19 days	06:19	13:37	20:56	Aries	00:23	04:00	07:32	Scorpius
2024	Apr	28	20 days	06:17	13:37	20:57	Aries	01:34	04:56	08:16	Ophiuchus
2024	Apr	29	21 days	06:16	13:37	20:59	Aries	02:34	05:54	09:17	Sagittarius
2024	Apr	30	22 days	06:14	13:36	21:00	Aries	03:18	06:52	10:32	Sagittarius
2024	May	01	23 days	06:12	13:36	21:02	Aries	03:50	07:49	11:57	Capricornus
2024	May	02	24 days	06:10	13:36	21:04	Aries	04:13	08:43	13:25	Capricornus
2024	May	03	25 days	06:08	13:36	21:05	Aries	04:31	09:34	14:52	Aquarius
2024	May	04	26 days	06:06	13:36	21:07	Aries	04:46	10:24	16:20	Aquarius
2024	May	05	27 days	06:05	13:36	21:08	Aries	04:59	11:13	17:47	Pisces
2024	May	06	28 days	06:03	13:36	21:10	Aries	05:12	12:04	19:16	Pisces
2024	May	07	29 days	06:01	13:36	21:11	Aries	05:28	12:56	20:47	Aries
2024	May	08	30 days	06:00	13:36	21:13	Aries	05:47	13:51	22:17	Aries
2024	May	09	01 days	05:58	13:36	21:15	Aries	06:13	14:49	23:44	Taurus");

    strtimes.do { |str|
      str.split($\n).collect(_.split($\t)).do { |a|
        //var arr = a.postln;
        var linemonth = months.indexOf(a[1].asSymbol) + 1;
        var lineyear = a[0].asInteger;
        var lineday = a[2].asInteger;
        var sunrise = a[4].split($:).collect(_.asInteger);
        var sunset = a[6].split($:).collect(_.asInteger);
        //[year, month, day, sunrise, sunset].postln;
        /*
        suntimes[year] ?? { suntimes[year] = () };
        suntimes[year][month] ?? { suntimes[year][month] = () };
        suntimes[year][month][day] = (sunrise: sunrise, sunset: sunset);
        */
        if ([year, month, day] == [lineyear, linemonth, lineday]) {
          ^super.newCopyArgs(sunrise, sunset);
        }
      };
    };
  }
}