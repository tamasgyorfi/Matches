package hu.bets.matches.gateway

object MockResponse {

  def getResponse : String = {
    """{"generated_at":"2018-06-23T19:01:43+00:00","schema":"http:\/\/schemas.sportradar.com\/bsa\/soccer\/v1\/json\/endpoints\/soccer\/schedule.json","sport_events":[{"id":"sr:match:12416766","scheduled":"2017-09-12T12:00:00+00:00","start_time_tbd":false,"status":"closed","tournament_round":{"type":"group","number":1,"group":"A"},"season":{"id":"sr:season:45144","name":"UEFA Youth League 17\/18","start_date":"2017-09-12","end_date":"2018-04-24","year":"17\/18","tournament_id":"sr:tournament:2324"},"tournament":{"id":"sr:tournament:2324","name":"UEFA Youth League","sport":{"id":"sr:sport:1","name":"Soccer"},"category":{"id":"sr:category:392","name":"International Youth"}},"competitors":[{"id":"sr:competitor:78135","name":"SL Benfica","country":"Portugal","country_code":"PRT","abbreviation":"BEN","qualifier":"home"},{"id":"sr:competitor:90126","name":"CSKA Moscow","country":"Russia","country_code":"RUS","abbreviation":"CSM","qualifier":"away"}],"venue":{"id":"sr:venue:8275","name":"Caixa Futebol Campus","capacity":2708,"city_name":"Seixal","country_name":"Portugal","map_coordinates":"38.639333,-9.091101","country_code":"PRT"}},{"id":"sr:match:12417082","scheduled":"2017-09-12T12:05:00+00:00","start_time_tbd":false,"status":"closed","tournament_round":{"type":"group","number":1,"group":"C"},"season":{"id":"sr:season:45144","name":"UEFA Youth League 17\/18","start_date":"2017-09-12","end_date":"2018-04-24","year":"17\/18","tournament_id":"sr:tournament:2324"},"tournament":{"id":"sr:tournament:2324","name":"UEFA Youth League","sport":{"id":"sr:sport:1","name":"Soccer"},"category":{"id":"sr:category:392","name":"International Youth"}},"competitors":[{"id":"sr:competitor:90124","name":"Chelsea London","country":"England","country_code":"ENG","abbreviation":"CLO","qualifier":"home"},{"id":"sr:competitor:253261","name":"Qarabag FK","country":"Azerbaijan","country_code":"AZE","abbreviation":"QAR","qualifier":"away"}]},{"id":"sr:match:12417080","scheduled":"2019-09-12T13:00:00+00:00","start_time_tbd":false,"status":"closed","tournament_round":{"type":"group","number":1,"group":"C"},"season":{"id":"sr:season:45144","name":"UEFA Youth League 17\/18","start_date":"2017-09-12","end_date":"2018-04-24","year":"17\/18","tournament_id":"sr:tournament:2324"},"tournament":{"id":"sr:tournament:2324","name":"UEFA Youth League","sport":{"id":"sr:sport:1","name":"Soccer"},"category":{"id":"sr:category:392","name":"International Youth"}},"competitors":[{"id":"sr:competitor:42731","name":"AS Roma","country":"Italy","country_code":"ITA","abbreviation":"ASR","qualifier":"home"},{"id":"sr:competitor:72022","name":"Atletico Madrid","country":"Spain","country_code":"ESP","abbreviation":"ATL","qualifier":"away"}]},{"id":"sr:match:12416518","scheduled":"2017-09-12T14:00:00+00:00","start_time_tbd":false,"status":"closed","tournament_round":{"type":"group","number":1,"group":"A"},"season":{"id":"sr:season:45144","name":"UEFA Youth League 17\/18","start_date":"2017-09-12","end_date":"2018-04-24","year":"17\/18","tournament_id":"sr:tournament:2324"},"tournament":{"id":"sr:tournament:2324","name":"UEFA Youth League","sport":{"id":"sr:sport:1","name":"Soccer"},"category":{"id":"sr:category:392","name":"International Youth"}},"competitors":[{"id":"sr:competitor:120848","name":"Manchester United","country":"England","country_code":"ENG","abbreviation":"MUN","qualifier":"home"},{"id":"sr:competitor:120858","name":"FC Basel","country":"Switzerland","country_code":"CHE","abbreviation":"BAS","qualifier":"away"}],"venue":{"id":"sr:venue:14454","name":"Leigh Sports Village","capacity":11000,"city_name":"Leigh","country_name":"England","country_code":"ENG"}},{"id":"sr:match:12417200","scheduled":"2017-09-12T14:00:00+00:00","start_time_tbd":false,"status":"closed","tournament_round":{"type":"group","number":1,"group":"D"},"season":{"id":"sr:season:45144","name":"UEFA Youth League 17\/18","start_date":"2017-09-12","end_date":"2018-04-24","year":"17\/18","tournament_id":"sr:tournament:2324"},"tournament":{"id":"sr:tournament:2324","name":"UEFA Youth League","sport":{"id":"sr:sport:1","name":"Soccer"},"category":{"id":"sr:category:392","name":"International Youth"}},"competitors":[{"id":"sr:competitor:90128","name":"FC Barcelona","country":"Spain","country_code":"ESP","abbreviation":"BAR","qualifier":"home"},{"id":"sr:competitor:42729","name":"Juventus Turin","country":"Italy","country_code":"ITA","abbreviation":"JUV","qualifier":"away"}],"venue":{"id":"sr:venue:11509","name":"Mini Estadi","capacity":15276,"city_name":"Barcelona","country_name":"Spain","map_coordinates":"41.379722,2.118056","country_code":"ESP"}},{"id":"sr:match:12417142","scheduled":"2017-09-12T15:00:00+00:00","start_time_tbd":false,"status":"closed","tournament_round":{"type":"group","number":1,"group":"D"},"season":{"id":"sr:season:45144","name":"UEFA Youth League 17\/18","start_date":"2017-09-12","end_date":"2018-04-24","year":"17\/18","tournament_id":"sr:tournament:2324"},"tournament":{"id":"sr:tournament:2324","name":"UEFA Youth League","sport":{"id":"sr:sport:1","name":"Soccer"},"category":{"id":"sr:category:392","name":"International Youth"}},"competitors":[{"id":"sr:competitor:90136","name":"Olympiacos FC","country":"Greece","country_code":"GRC","abbreviation":"OLY","qualifier":"home"},{"id":"sr:competitor:78151","name":"Sporting Lissabon","country":"Portugal","country_code":"PRT","abbreviation":"SPO","qualifier":"away"}],"venue":{"id":"sr:venue:23548","name":"Olympiacos FC Training Centre","city_name":"Piraeus","country_name":"Greece","country_code":"GRC"}},{"id":"sr:match:12416962","scheduled":"2017-09-12T16:00:00+00:00","start_time_tbd":false,"status":"closed","tournament_round":{"type":"group","number":1,"group":"B"},"season":{"id":"sr:season:45144","name":"UEFA Youth League 17\/18","start_date":"2017-09-12","end_date":"2018-04-24","year":"17\/18","tournament_id":"sr:tournament:2324"},"tournament":{"id":"sr:tournament:2324","name":"UEFA Youth League","sport":{"id":"sr:sport:1","name":"Soccer"},"category":{"id":"sr:category:392","name":"International Youth"}},"competitors":[{"id":"sr:competitor:120866","name":"Celtic FC","country":"Scotland","country_code":"SCO","abbreviation":"CEL","qualifier":"home"},{"id":"sr:competitor:90138","name":"Paris St.-Germain","country":"France","country_code":"FRA","abbreviation":"PSG","qualifier":"away"}],"venue":{"id":"sr:venue:3379","name":"YOUR Radio 103FM Stadium","capacity":2020,"city_name":"Dumbarton","country_name":"Scotland","map_coordinates":"55.938447,-4.561439","country_code":"SCO"}},{"id":"sr:match:12416964","scheduled":"2017-09-12T16:00:00+00:00","start_time_tbd":false,"status":"closed","tournament_round":{"type":"group","number":1,"group":"B"},"season":{"id":"sr:season:45144","name":"UEFA Youth League 17\/18","start_date":"2017-09-12","end_date":"2018-04-24","year":"17\/18","tournament_id":"sr:tournament:2324"},"tournament":{"id":"sr:tournament:2324","name":"UEFA Youth League","sport":{"id":"sr:sport:1","name":"Soccer"},"category":{"id":"sr:category:392","name":"International Youth"}},"competitors":[{"id":"sr:competitor:43728","name":"Bayern Munich","abbreviation":"FCB","qualifier":"home"},{"id":"sr:competitor:90144","name":"RSC Anderlecht","country":"Belgium","country_code":"BEL","abbreviation":"RSA","qualifier":"away"}],"venue":{"id":"sr:venue:23512","name":"FC Bayern Campus","capacity":2500,"city_name":"Munich","country_name":"Germany","country_code":"DEU"}},{"id":"sr:match:12330394","scheduled":"2017-09-12T18:45:00+00:00","start_time_tbd":false,"status":"closed","tournament_round":{"type":"group","number":1,"group":"A"},"season":{"id":"sr:season:41198","name":"UEFA Champions League 17\/18","start_date":"2017-06-27","end_date":"2018-05-27","year":"17\/18","tournament_id":"sr:tournament:7"},"tournament":{"id":"sr:tournament:7","name":"UEFA Champions League","sport":{"id":"sr:sport:1","name":"Soccer"},"category":{"id":"sr:category:393","name":"International Clubs"}},"competitors":[{"id":"sr:competitor:3006","name":"Benfica Lisbon","country":"Portugal","country_code":"PRT","abbreviation":"BEN","qualifier":"home"},{"id":"sr:competitor:2325","name":"CSKA Moscow","country":"Russia","country_code":"RUS","abbreviation":"CSK","qualifier":"away"}],"venue":{"id":"sr:venue:902","name":"Estadio do Sport Lisboa e Benfica","capacity":64642,"city_name":"Lisbon","country_name":"Portugal","map_coordinates":"38.752670,-9.184697","country_code":"PRT"}},{"id":"sr:match:12330396","scheduled":"2017-09-12T18:45:00+00:00","start_time_tbd":false,"status":"closed","tournament_round":{"type":"group","number":1,"group":"A"},"season":{"id":"sr:season:41198","name":"UEFA Champions League 17\/18","start_date":"2017-06-27","end_date":"2018-05-27","year":"17\/18","tournament_id":"sr:tournament:7"},"tournament":{"id":"sr:tournament:7","name":"UEFA Champions League","sport":{"id":"sr:sport:1","name":"Soccer"},"category":{"id":"sr:category:393","name":"International Clubs"}},"competitors":[{"id":"sr:competitor:35","name":"Manchester United","country":"England","country_code":"ENG","abbreviation":"MUN","qualifier":"home"},{"id":"sr:competitor:2501","name":"FC Basel 1893","country":"Switzerland","country_code":"CHE","abbreviation":"FCB","qualifier":"away"}],"venue":{"id":"sr:venue:9","name":"Old Trafford","capacity":75635,"city_name":"Manchester","country_name":"England","map_coordinates":"53.463150,-2.291444","country_code":"ENG"}},{"id":"sr:match:12330398","scheduled":"2017-09-12T18:45:00+00:00","start_time_tbd":false,"status":"closed","tournament_round":{"type":"group","number":1,"group":"B"},"season":{"id":"sr:season:41198","name":"UEFA Champions League 17\/18","start_date":"2017-06-27","end_date":"2018-05-27","year":"17\/18","tournament_id":"sr:tournament:7"},"tournament":{"id":"sr:tournament:7","name":"UEFA Champions League","sport":{"id":"sr:sport:1","name":"Soccer"},"category":{"id":"sr:category:393","name":"International Clubs"}},"competitors":[{"id":"sr:competitor:2672","name":"Bayern Munich","country":"Germany","country_code":"DEU","abbreviation":"FCB","qualifier":"home"},{"id":"sr:competitor:2900","name":"RSC Anderlecht","country":"Belgium","country_code":"BEL","abbreviation":"RSC","qualifier":"away"}],"venue":{"id":"sr:venue:574","name":"Allianz Arena","capacity":75000,"city_name":"Munich","country_name":"Germany","map_coordinates":"48.218777,11.624748","country_code":"DEU"}},{"id":"sr:match:12330400","scheduled":"2017-09-12T18:45:00+00:00","start_time_tbd":false,"status":"closed","tournament_round":{"type":"group","number":1,"group":"B"},"season":{"id":"sr:season:41198","name":"UEFA Champions League 17\/18","start_date":"2017-06-27","end_date":"2018-05-27","year":"17\/18","tournament_id":"sr:tournament:7"},"tournament":{"id":"sr:tournament:7","name":"UEFA Champions League","sport":{"id":"sr:sport:1","name":"Soccer"},"category":{"id":"sr:category:393","name":"International Clubs"}},"competitors":[{"id":"sr:competitor:2352","name":"Celtic FC","country":"Scotland","country_code":"SCO","abbreviation":"CEL","qualifier":"home"},{"id":"sr:competitor:1644","name":"Paris Saint-Germain","country":"France","country_code":"FRA","abbreviation":"PSG","qualifier":"away"}],"venue":{"id":"sr:venue:916","name":"Celtic Park","capacity":60411,"city_name":"Glasgow","country_name":"Scotland","map_coordinates":"55.849711,-4.205589","country_code":"SCO"}},{"id":"sr:match:12330402","scheduled":"2017-09-12T18:45:00+00:00","start_time_tbd":false,"status":"closed","tournament_round":{"type":"group","number":1,"group":"C"},"season":{"id":"sr:season:41198","name":"UEFA Champions League 17\/18","start_date":"2017-06-27","end_date":"2018-05-27","year":"17\/18","tournament_id":"sr:tournament:7"},"tournament":{"id":"sr:tournament:7","name":"UEFA Champions League","sport":{"id":"sr:sport:1","name":"Soccer"},"category":{"id":"sr:category:393","name":"International Clubs"}},"competitors":[{"id":"sr:competitor:38","name":"Chelsea FC","country":"England","country_code":"ENG","abbreviation":"CHE","qualifier":"home"},{"id":"sr:competitor:5962","name":"Qarabag FK","country":"Azerbaijan","country_code":"AZE","abbreviation":"QAR","qualifier":"away"}],"venue":{"id":"sr:venue:799","name":"Stamford Bridge","capacity":41798,"city_name":"London","country_name":"England","map_coordinates":"51.481579,-0.191094","country_code":"ENG"}},{"id":"sr:match:12330404","scheduled":"2017-09-12T18:45:00+00:00","start_time_tbd":false,"status":"closed","tournament_round":{"type":"group","number":1,"group":"C"},"season":{"id":"sr:season:41198","name":"UEFA Champions League 17\/18","start_date":"2017-06-27","end_date":"2018-05-27","year":"17\/18","tournament_id":"sr:tournament:7"},"tournament":{"id":"sr:tournament:7","name":"UEFA Champions League","sport":{"id":"sr:sport:1","name":"Soccer"},"category":{"id":"sr:category:393","name":"International Clubs"}},"competitors":[{"id":"sr:competitor:2702","name":"AS Roma","country":"Italy","country_code":"ITA","abbreviation":"ROM","qualifier":"home"},{"id":"sr:competitor:2836","name":"Atletico Madrid","country":"Spain","country_code":"ESP","abbreviation":"ATM","qualifier":"away"}],"venue":{"id":"sr:venue:870","name":"Stadio Olimpico","capacity":72698,"city_name":"Rome","country_name":"Italy","map_coordinates":"41.933886,12.454786","country_code":"ITA"}},{"id":"sr:match:12330406","scheduled":"2017-09-12T18:45:00+00:00","start_time_tbd":false,"status":"closed","tournament_round":{"type":"group","number":1,"group":"D"},"season":{"id":"sr:season:41198","name":"UEFA Champions League 17\/18","start_date":"2017-06-27","end_date":"2018-05-27","year":"17\/18","tournament_id":"sr:tournament:7"},"tournament":{"id":"sr:tournament:7","name":"UEFA Champions League","sport":{"id":"sr:sport:1","name":"Soccer"},"category":{"id":"sr:category:393","name":"International Clubs"}},"competitors":[{"id":"sr:competitor:2817","name":"FC Barcelona","country":"Spain","country_code":"ESP","abbreviation":"FCB","qualifier":"home"},{"id":"sr:competitor:2687","name":"Juventus Turin","country":"Italy","country_code":"ITA","abbreviation":"JUV","qualifier":"away"}],"venue":{"id":"sr:venue:709","name":"Camp Nou","capacity":98787,"city_name":"Barcelona","country_name":"Spain","map_coordinates":"41.380890,2.122813","country_code":"ESP"}},{"id":"sr:match:12330408","scheduled":"2017-09-12T18:45:00+00:00","start_time_tbd":false,"status":"closed","tournament_round":{"type":"group","number":1,"group":"D"},"season":{"id":"sr:season:41198","name":"UEFA Champions League 17\/18","start_date":"2017-06-27","end_date":"2018-05-27","year":"17\/18","tournament_id":"sr:tournament:7"},"tournament":{"id":"sr:tournament:7","name":"UEFA Champions League","sport":{"id":"sr:sport:1","name":"Soccer"},"category":{"id":"sr:category:393","name":"International Clubs"}},"competitors":[{"id":"sr:competitor:3245","name":"Olympiacos FC","country":"Greece","country_code":"GRC","abbreviation":"OLY","qualifier":"home"},{"id":"sr:competitor:3001","name":"Sporting CP","country":"Portugal","country_code":"PRT","abbreviation":"SPO","qualifier":"away"}],"venue":{"id":"sr:venue:906","name":"Georgios Karaiskakis","capacity":32115,"city_name":"Piraeus","country_name":"Greece","map_coordinates":"37.946447,23.664317","country_code":"GRC"}}]}"""
  }
}
