//package by.aurorasoft.distanceclassifier.it.citycrud;
//
//import by.aurorasoft.distanceclassifier.controller.city.model.CityRequest;
//import by.aurorasoft.distanceclassifier.crud.model.entity.CityEntity;
//import by.aurorasoft.distanceclassifier.it.base.AbstractIT;
//import by.aurorasoft.distanceclassifier.model.CityType;
//import by.aurorasoft.distanceclassifier.testutil.GeometryUtil;
//import org.junit.jupiter.api.Test;
//import org.locationtech.jts.geom.GeometryFactory;
//import org.locationtech.jts.geom.Polygon;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.wololo.geojson.Geometry;
//import org.wololo.jts2geojson.GeoJSONWriter;
//
//import static by.aurorasoft.distanceclassifier.model.CityType.TOWN;
//import static by.aurorasoft.distanceclassifier.testutil.CityEntityUtil.checkEquals;
//import static by.aurorasoft.distanceclassifier.testutil.HttpUtil.*;
//import static org.junit.jupiter.api.Assertions.assertFalse;
//import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;
//import static org.springframework.web.util.UriComponentsBuilder.fromUriString;
//
//public final class CityCrudIT extends AbstractIT {
//    private static final String URL = "/api/v1/city";
//    private static final String PARAM_NAME_PAGE_NUMBER = "pageNumber";
//    private static final String PARAM_NAME_PAGE_SIZE = "pageSize";
//
//    @Autowired
//    private GeometryFactory geometryFactory;
//
//    @Autowired
//    private GeoJSONWriter geoJSONWriter;
//
//    @Test
//    public void allCitiesShouldBeFound()
//            throws Exception {
//        final String url = createUrlToFindAllCities(0, 2);
//        final String actual = getExpectingOk(restTemplate, url, String.class);
//        final String expected = "{\"content\":[{\"id\":22,\"name\":\"Высокое\",\"geometry\":{\"type\":\"Polygon\",\"coordinates\":[[[23.3477814,52.369099],[23.3485152,52.3685276],[23.3500551,52.3679344],[23.3553553,52.3680758],[23.3555171,52.3681363],[23.357017,52.3686965],[23.3577495,52.3689701],[23.3579689,52.3684862],[23.3574161,52.3684082],[23.3558005,52.3676774],[23.3583846,52.3671124],[23.3636188,52.3667439],[23.3631733,52.3657513],[23.3655443,52.364998],[23.3709194,52.3635108],[23.3741139,52.3651564],[23.3753139,52.3646364],[23.3770339,52.3642064],[23.3786639,52.3619764],[23.3789239,52.3604364],[23.3786639,52.3595764],[23.3779739,52.3590564],[23.3840739,52.3587964],[23.3856139,52.3578564],[23.3888839,52.3577664],[23.3915783,52.3588263],[23.3956639,52.3612864],[23.3972939,52.3617442],[23.4002494,52.3579989],[23.4011184,52.3582283],[23.4039909,52.358507],[23.4055711,52.359213],[23.4058764,52.3595171],[23.4079164,52.3588878],[23.409106,52.3602314],[23.409423,52.3607774],[23.4054089,52.3622789],[23.4059887,52.3643637],[23.3949739,52.3675564],[23.3893724,52.3676254],[23.3876524,52.3679654],[23.3868824,52.3683954],[23.3855924,52.3687354],[23.3853639,52.3685864],[23.3826139,52.3693564],[23.3779739,52.3698765],[23.3772091,52.3740474],[23.3757876,52.3751432],[23.373413,52.3740239],[23.3688675,52.3778154],[23.3718519,52.3822833],[23.3719671,52.3824559],[23.3722578,52.382891],[23.3712601,52.3830416],[23.3701872,52.3812341],[23.3653639,52.3822365],[23.3611451,52.3814409],[23.3578773,52.3795968],[23.3572627,52.3789231],[23.3518277,52.3754654],[23.3534404,52.3736478],[23.3477814,52.369099]]]},\"type\":\"CITY\"},{\"id\":23,\"name\":\"Брест\",\"geometry\":{\"type\":\"Polygon\",\"coordinates\":[[[23.5654232,52.1162631],[23.5659766,52.116096],[23.566275,52.1160059],[23.5671931,52.1157217],[23.5680507,52.1157651],[23.568371,52.1157813],[23.5695066,52.1158315],[23.5710303,52.116255],[23.5723918,52.11684],[23.5737812,52.1175824],[23.5753623,52.1181174],[23.5764054,52.1184118],[23.577355,52.1194764],[23.5782358,52.1198954],[23.5793398,52.120041],[23.5805768,52.119904],[23.5816218,52.1194435],[23.5825702,52.1189744],[23.583568,52.1182221],[23.5845679,52.1176411],[23.5858275,52.1173044],[23.5872984,52.1170653],[23.5886803,52.1170508],[23.5904592,52.1171015],[23.5921511,52.1172247],[23.5939954,52.1174586],[23.5957807,52.1178789],[23.5969302,52.1178149],[23.5985699,52.1173874],[23.6013049,52.1160382],[23.6028573,52.1150088],[23.6034562,52.1145945],[23.6043516,52.1139757],[23.6053522,52.1129117],[23.6061424,52.1118217],[23.606876,52.1109692],[23.6073742,52.1100788],[23.6078002,52.1091826],[23.6084439,52.1086937],[23.6096005,52.1082153],[23.610698,52.1080447],[23.6124436,52.1083043],[23.6142018,52.1087136],[23.6152395,52.1092169],[23.6167695,52.1093171],[23.6176482,52.1090667],[23.618544,52.1088044],[23.6199667,52.107677],[23.6204709,52.1068052],[23.6204162,52.1059973],[23.6198787,52.1053106],[23.6191534,52.1048994],[23.6181878,52.1048737],[23.6166225,52.1047222],[23.6153028,52.1046675],[23.6136126,52.1042573],[23.6127193,52.1039327],[23.6122044,52.1036105],[23.6117087,52.1032717],[23.6112838,52.1027149],[23.6107345,52.1018074],[23.6106401,52.1010515],[23.6104845,52.1002758],[23.6111118,52.0991158],[23.6115658,52.098219],[23.6120316,52.0976932],[23.6129296,52.0967593],[23.6142148,52.096328],[23.6149037,52.0954621],[23.6154922,52.0941504],[23.6166318,52.0932313],[23.6166498,52.0932168],[23.6170216,52.0928265],[23.6178901,52.0922626],[23.6188766,52.0916041],[23.62003,52.0909861],[23.6212466,52.0902696],[23.6218523,52.0900033],[23.6247732,52.0889525],[23.6258391,52.0886476],[23.6263125,52.0884834],[23.6271459,52.0881944],[23.6281332,52.0880812],[23.6311529,52.0877723],[23.6316695,52.0877191],[23.6333725,52.0874666],[23.6345543,52.087097],[23.6356705,52.0867097],[23.6375866,52.086092],[23.6389739,52.0858026],[23.6401207,52.0855799],[23.6394859,52.0845654],[23.6396469,52.0838072],[23.6400975,52.0834776],[23.640972,52.0829078],[23.6410783,52.0825191],[23.6406305,52.0819931],[23.640326,52.0814844],[23.6402762,52.080912],[23.6404864,52.0804207],[23.6409676,52.0800213],[23.6417893,52.0795301],[23.6422836,52.0790754],[23.6426068,52.0776673],[23.6428858,52.0771952],[23.6435646,52.0769938],[23.6447084,52.0768768],[23.6461326,52.0765608],[23.6467695,52.0756514],[23.6476231,52.0749869],[23.6480411,52.0748105],[23.6486849,52.0748415],[23.6494398,52.0749934],[23.6506627,52.0752425],[23.6513513,52.0752305],[23.6517859,52.0749422],[23.6520312,52.0746943],[23.6529424,52.0737576],[23.6536407,52.0727295],[23.6537423,52.0725114],[23.6538793,52.0721235],[23.6538129,52.0715539],[23.6535566,52.0711155],[23.6531038,52.0706439],[23.6524171,52.0701075],[23.6514573,52.0695568],[23.6507803,52.0691685],[23.6505503,52.0689482],[23.6493392,52.067788],[23.6486761,52.0673983],[23.6480618,52.0671854],[23.6464456,52.0670803],[23.6457346,52.0669165],[23.6449981,52.0666809],[23.6444005,52.0662627],[23.6441434,52.0657721],[23.6441892,52.0650742],[23.6444238,52.0645393],[23.6449012,52.0640054],[23.6454477,52.0635558],[23.646115,52.0632932],[23.6471267,52.063035],[23.6483702,52.0628852],[23.6499055,52.0630231],[23.6516564,52.0633997],[23.6530115,52.0632302],[23.6540479,52.0628213],[23.6547388,52.0623773],[23.6553815,52.0616729],[23.6558128,52.0605436],[23.6558396,52.0595759],[23.6555886,52.0589855],[23.6549191,52.058529],[23.6542539,52.058401],[23.6534471,52.0584472],[23.6525641,52.0586141],[23.6517566,52.0589721],[23.6510085,52.0595667],[23.6504934,52.0600475],[23.6490772,52.0604057],[23.6480408,52.0605647],[23.647745,52.0606003],[23.6472351,52.0606617],[23.6461032,52.0607283],[23.6451805,52.0609532],[23.6442042,52.0610594],[23.6431742,52.0609202],[23.6418932,52.0604255],[23.6415166,52.0596623],[23.6414522,52.058938],[23.6419704,52.0581767],[23.643083,52.0577064],[23.6440776,52.0574432],[23.6453468,52.0573732],[23.6464843,52.0573324],[23.6479551,52.0572102],[23.6491913,52.0568495],[23.6502526,52.0565405],[23.6512319,52.0561799],[23.651948,52.0559178],[23.6530217,52.0553539],[23.6538116,52.0548335],[23.6542711,52.0542945],[23.6546433,52.053857],[23.6553362,52.0527001],[23.6558486,52.0516538],[23.6559705,52.0510041],[23.656008,52.0501339],[23.6555746,52.048431],[23.655639,52.0479137],[23.655992,52.0470717],[23.6567119,52.0462317],[23.656949,52.0458767],[23.6571434,52.0451987],[23.657375,52.0445577],[23.6576094,52.0441164],[23.6576754,52.043687],[23.6578271,52.0432626],[23.6580208,52.042866],[23.6583265,52.0422909],[23.6589574,52.0418346],[23.6607495,52.0412299],[23.6627227,52.0399492],[23.6627396,52.0396771],[23.6747887,52.0423664],[23.6753331,52.042241],[23.6784296,52.0395543],[23.6823832,52.0373177],[23.6854769,52.036683],[23.685743,52.0353686],[23.6917196,52.0308698],[23.6922299,52.0308172],[23.694248,52.0309813],[23.7036599,52.0266239],[23.7024453,52.025096],[23.709989,52.023784],[23.710662,52.02613],[23.712247,52.02666],[23.7297551,52.0259385],[23.7292273,52.0300933],[23.7286723,52.0344625],[23.7283708,52.0374065],[23.7280087,52.040941],[23.7396916,52.0409395],[23.7439569,52.040939],[23.7460283,52.0413764],[23.7476657,52.0420002],[23.7489518,52.0427513],[23.7494219,52.0431463],[23.7602785,52.0532162],[23.7605407,52.0534532],[23.7617436,52.0545403],[23.7636662,52.0557929],[23.7638419,52.0558985],[23.7649878,52.0551741],[23.7662576,52.0543169],[23.7671365,52.0537191],[23.7668783,52.0541153],[23.7666656,52.0544272],[23.7663518,52.0550827],[23.766257,52.0555345],[23.7662557,52.0559413],[23.7663358,52.056395],[23.7664521,52.0567099],[23.7674007,52.0580838],[23.7675254,52.0582523],[23.7795898,52.0724517],[23.7818834,52.0782515],[23.7835251,52.0817673],[23.784647,52.085151],[23.788648,52.08509],[23.7890604,52.080386],[23.8205808,52.0768362],[23.833801,52.07135],[23.8413848,52.0748322],[23.8410101,52.0752002],[23.835332,52.080776],[23.837726,52.083354],[23.8270043,52.0923918],[23.8257301,52.0924935],[23.8257301,52.0942567],[23.8254109,52.0958288],[23.8234079,52.0967931],[23.8131136,52.0968279],[23.8134102,52.1031605],[23.822189,52.104377],[23.8229553,52.0969846],[23.8241585,52.0965996],[23.8267089,52.0960252],[23.8331537,52.0966614],[23.8349242,52.0974218],[23.8370814,52.0977031],[23.8407048,52.0982495],[23.8385505,52.1061394],[23.854412,52.107612],[23.854011,52.1173888],[23.8506737,52.1167181],[23.8385295,52.1140431],[23.8171834,52.1105451],[23.8131534,52.1183702],[23.8117874,52.1210226],[23.8133105,52.1218735],[23.8086246,52.1297487],[23.8083631,52.1301642],[23.808034,52.130687],[23.8011111,52.1324985],[23.7973267,52.133493],[23.7928204,52.1346771],[23.7923914,52.1347898],[23.7495239,52.146273],[23.7411466,52.1484823],[23.7360381,52.1491334],[23.7309075,52.1495122],[23.7167485,52.1505576],[23.691895,52.1523702],[23.6911747,52.1524458],[23.6791209,52.1501938],[23.670953,52.1478903],[23.6353687,52.1396583],[23.6302465,52.1384732],[23.6293375,52.1382629],[23.5787811,52.1260673],[23.5726259,52.1256116],[23.5715095,52.1259594],[23.5688016,52.1236744],[23.5687898,52.1203781],[23.5654232,52.1162631]]]},\"type\":\"CITY\"}],\"pageable\":{\"sort\":{\"empty\":false,\"unsorted\":false,\"sorted\":true},\"offset\":0,\"pageNumber\":0,\"pageSize\":2,\"paged\":true,\"unpaged\":false},\"totalPages\":74,\"totalElements\":148,\"last\":false,\"size\":2,\"number\":0,\"sort\":{\"empty\":false,\"unsorted\":false,\"sorted\":true},\"numberOfElements\":2,\"first\":true,\"empty\":false}";
//        assertEquals(expected, actual, true);
//    }
//
//    @Test
//    public void cityShouldBeSaved()
//            throws Exception {
//        final String givenName = "name";
//        final String givenGeometryText = "POLYGON((1 1, 2 1, 2 2, 1 1))";
//        final CityType givenType = TOWN;
//        final CityRequest givenRequest = new CityRequest(givenName, createWololoPolygon(givenGeometryText), givenType);
//
//        final String actual = postExpectingOk(restTemplate, URL, givenRequest, String.class);
//        final String expected = """
//                {
//                  "id": 1,
//                  "name": "name",
//                  "geometry": {
//                    "type": "Polygon",
//                    "coordinates": [
//                      [
//                        [
//                          1,
//                          1
//                        ],
//                        [
//                          2,
//                          1
//                        ],
//                        [
//                          2,
//                          2
//                        ],
//                        [
//                          1,
//                          1
//                        ]
//                      ]
//                    ]
//                  },
//                  "type": "TOWN"
//                }""";
//        assertEquals(expected, actual, true);
//
//        final Long expectedId = 1L;
//        final CityEntity actualCity = findCity(expectedId);
//        final CityEntity expectedCity = CityEntity.builder()
//                .id(expectedId)
//                .name(givenName)
//                .geometry(createPolygon(givenGeometryText))
//                .type(givenType)
//                .boundingBox(createPolygon("POLYGON((1 1, 1 2, 2 2, 2 1, 1 1))"))
//                .build();
//        checkEquals(expectedCity, actualCity);
//    }
//
//    @Test
//    public void cityShouldBeUpdated()
//            throws Exception {
//        final Long givenId = 24L;
//        final String givenName = "name";
//        final String givenGeometryText = "POLYGON((1 1, 2 1, 2 2, 1 1))";
//        final CityType givenType = TOWN;
//        final CityRequest givenRequest = new CityRequest(givenName, createWololoPolygon(givenGeometryText), givenType);
//
//        final String url = createUrlWithPathVariable(givenId);
//        final String actual = putExpectingOk(restTemplate, url, givenRequest, String.class);
//        final String expected = """
//                {
//                  "id": 24,
//                  "name": "name",
//                  "geometry": {
//                    "type": "Polygon",
//                    "coordinates": [
//                      [
//                        [
//                          1,
//                          1
//                        ],
//                        [
//                          2,
//                          1
//                        ],
//                        [
//                          2,
//                          2
//                        ],
//                        [
//                          1,
//                          1
//                        ]
//                      ]
//                    ]
//                  },
//                  "type": "TOWN"
//                }""";
//        assertEquals(expected, actual, true);
//
//        final CityEntity actualCity = findCity(givenId);
//        final CityEntity expectedCity = CityEntity.builder()
//                .id(givenId)
//                .name(givenName)
//                .geometry(createPolygon(givenGeometryText))
//                .type(givenType)
//                .boundingBox(createPolygon("POLYGON((1 1, 1 2, 2 2, 2 1, 1 1))"))
//                .build();
//        checkEquals(expectedCity, actualCity);
//    }
//
//    @Test
//    public void cityShouldBeDeleted() {
//        final Long givenId = 24L;
//
//        final String url = createUrlWithPathVariable(givenId);
//        deleteExpectingNoContent(restTemplate, url);
//
//        assertFalse(isCityExist(givenId));
//    }
//
//    @SuppressWarnings("SameParameterValue")
//    private static String createUrlToFindAllCities(final int pageNumber, final int pageSize) {
//        return fromUriString(URL)
//                .queryParam(PARAM_NAME_PAGE_NUMBER, pageNumber)
//                .queryParam(PARAM_NAME_PAGE_SIZE, pageSize)
//                .build()
//                .toUriString();
//    }
//
//    private Polygon createPolygon(final String text) {
//        return GeometryUtil.createPolygon(text, geometryFactory);
//    }
//
//    @SuppressWarnings("SameParameterValue")
//    private Geometry createWololoPolygon(final String text) {
//        final Polygon polygon = createPolygon(text);
//        return geoJSONWriter.write(polygon);
//    }
//
//    private CityEntity findCity(final Long id) {
//        return entityManager.find(CityEntity.class, id);
//    }
//
//    private boolean isCityExist(final Long id) {
//        return findCity(id) != null;
//    }
//
//    private static String createUrlWithPathVariable(final Object value) {
//        return URL + "/" + value;
//    }
//}
