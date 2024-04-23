package by.aurorasoft.nominatim.it.cityscan;

import by.aurorasoft.nominatim.base.AbstractSpringBootTest;
import by.aurorasoft.nominatim.controller.cityscan.model.AreaCoordinateRequest;
import by.aurorasoft.nominatim.crud.model.entity.CityEntity;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.GeometryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Stream;

import static by.aurorasoft.nominatim.model.CityType.TOWN;
import static by.aurorasoft.nominatim.testutil.CityEntityUtil.checkEqualsExceptId;
import static by.aurorasoft.nominatim.testutil.GeometryUtil.createMultipolygon;
import static by.aurorasoft.nominatim.testutil.GeometryUtil.createPolygonByText;
import static by.aurorasoft.nominatim.testutil.HttpUtil.postExpectingNoContext;
import static java.util.Comparator.comparing;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD;
import static org.springframework.transaction.annotation.Propagation.NOT_SUPPORTED;

@Transactional(propagation = NOT_SUPPORTED)
@SpringBootTest(webEnvironment = RANDOM_PORT)
@DirtiesContext(classMode = AFTER_EACH_TEST_METHOD)
public class CityScanIT extends AbstractSpringBootTest {
    private static final String URL = "/api/v1/cityScan";

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private GeometryFactory geometryFactory;

    @Test
    @Sql(statements = "DELETE FROM city")
    public void citiesShouldBeScanned() {
        final AreaCoordinateRequest givenRequest = new AreaCoordinateRequest(
                53.276930,
                29.073363,
                54.109940,
                30.180785
        );

        postExpectingNoContext(restTemplate, URL, givenRequest);

        final List<CityEntity> actual = findCitiesOrderedByGeometry();
        final List<CityEntity> expected = getExpectedCitiesOrderedByGeometry();
        checkEqualsExceptId(expected, actual);
    }

    private List<CityEntity> findCitiesOrderedByGeometry() {
        return entityManager.createQuery("SELECT e FROM CityEntity e ORDER BY e.geometry", CityEntity.class).getResultList();
    }

    @SuppressWarnings("unchecked")
    private List<CityEntity> getExpectedCitiesOrderedByGeometry() {
        return getExpectedCities().sorted(comparing(CityEntity::getGeometry)).toList();
    }

    private Stream<CityEntity> getExpectedCities() {
        return Stream.of(
                CityEntity.builder()
                        .name("Быхаў")
                        .geometry(createMultipolygon("MULTIPOLYGON (((30.1865971 53.5043267, 30.1853337 53.5059397, 30.1849536 53.5076854, 30.1835165 53.5088331, 30.1843017 53.5092951, 30.184528 53.5107596, 30.184113 53.5113136, 30.183411 53.511167, 30.1831745 53.512015, 30.1840294 53.5123077, 30.1836053 53.5144264, 30.1838399 53.5167017, 30.1839564 53.517879, 30.1835576 53.5187766, 30.1813299 53.5217352, 30.1807678 53.5228186, 30.181935 53.5231599, 30.1802835 53.5237866, 30.1809341 53.5243653, 30.1772588 53.5268624, 30.1770268 53.5269968, 30.1812946 53.5294132, 30.1828628 53.5329404, 30.184335 53.5344331, 30.1902232 53.5366419, 30.190751 53.5366153, 30.1933524 53.5340365, 30.1949896 53.535005, 30.1995779 53.5386655, 30.2007155 53.5399031, 30.2034232 53.5405922, 30.2058093 53.541064, 30.2070854 53.5416061, 30.2090679 53.5429322, 30.2105447 53.5432562, 30.2132971 53.5422943, 30.2158453 53.5434002, 30.216018 53.5432369, 30.2161306 53.5428034, 30.2162701 53.5425866, 30.2167422 53.5423858, 30.2168012 53.5422934, 30.2168494 53.5421308, 30.2170104 53.542016, 30.2172625 53.541965, 30.2176456 53.542032, 30.2184802 53.5419204, 30.2191612 53.5419778, 30.219596 53.5420511, 30.2199072 53.542185, 30.2201164 53.5423539, 30.22039 53.5424081, 30.2206045 53.542338, 30.2206582 53.542185, 30.2206582 53.5420224, 30.2207494 53.5419619, 30.2210337 53.5418567, 30.2213126 53.5418758, 30.2216345 53.541828, 30.2219027 53.5419108, 30.2220958 53.5419077, 30.2222729 53.5418248, 30.222407 53.5416558, 30.2225357 53.5416303, 30.2226645 53.5416367, 30.2228308 53.5418535, 30.2229756 53.5419172, 30.2231902 53.54193, 30.2233189 53.5418917, 30.2233672 53.541812, 30.2233672 53.5417036, 30.2234101 53.5412733, 30.2235389 53.5411968, 30.2237427 53.5411777, 30.2241826 53.5412574, 30.224483 53.5412574, 30.2247673 53.5411936, 30.2250838 53.541031, 30.2251911 53.540929, 30.2252871 53.5408981, 30.2253789 53.5408685, 30.2256095 53.5408621, 30.2258241 53.540878, 30.225985 53.5408525, 30.2262372 53.5406421, 30.2264196 53.540572, 30.226602 53.5403903, 30.2267414 53.5401608, 30.2267843 53.539979, 30.226796 53.5399474, 30.2268434 53.5398197, 30.2270043 53.5397113, 30.227757 53.5395809, 30.2282675 53.5395834, 30.2286673 53.5395853, 30.2289864 53.5395837, 30.2291152 53.5395646, 30.2291635 53.5395136, 30.2291545 53.5394484, 30.2291501 53.5394164, 30.229083 53.539241, 30.2290669 53.5390928, 30.2291205 53.5389701, 30.2292091 53.5389079, 30.229421 53.5388537, 30.2296865 53.5387979, 30.2299577 53.53878, 30.2301317 53.5388473, 30.230239 53.5389318, 30.2304482 53.5392267, 30.2306038 53.5393247, 30.231007 53.5393737, 30.2317829 53.5393196, 30.2319131 53.5393339, 30.232017 53.5393453, 30.2321861 53.5394072, 30.2328117 53.539617, 30.2332829 53.5399533, 30.2336861 53.5400306, 30.2340907 53.5400269, 30.2343535 53.540046, 30.2345788 53.5399854, 30.2346257 53.5399431, 30.234713 53.5398643, 30.2352601 53.5396889, 30.2357537 53.5396443, 30.2358266 53.5396259, 30.2361453 53.5395455, 30.2364671 53.5395391, 30.236671 53.5395869, 30.2369875 53.5397655, 30.2370626 53.5398962, 30.2371645 53.5399599, 30.2373415 53.5399663, 30.2375293 53.5398643, 30.2378619 53.5397655, 30.2381033 53.5396539, 30.2382803 53.5396443, 30.2384466 53.5396858, 30.2385378 53.5398547, 30.2385002 53.5399567, 30.23835 53.5400842, 30.23835 53.5401767, 30.2384144 53.5402436, 30.2385539 53.5402723, 30.2386987 53.5402468, 30.2388757 53.5401544, 30.23927 53.54007, 30.2396429 53.539944, 30.2398628 53.539928, 30.2402329 53.5400109, 30.2404958 53.5400428, 30.2408347 53.5401168, 30.2409196 53.5401353, 30.2414024 53.5401448, 30.2418691 53.5401671, 30.2419603 53.5401471, 30.2422178 53.5400906, 30.2424377 53.5400237, 30.2425826 53.5398834, 30.2427596 53.5398005, 30.243001 53.5397782, 30.2433175 53.5398611, 30.2435374 53.5398515, 30.2437788 53.5397272, 30.2438915 53.5396252, 30.244326 53.5394052, 30.2446049 53.5393287, 30.2450234 53.5389366, 30.2451285 53.5388222, 30.2453989 53.538723, 30.2461585 53.5386389, 30.2463109 53.5386098, 30.2464572 53.5385633, 30.2465777 53.5385044, 30.2466846 53.5384322, 30.2477236 53.5375328, 30.2480647 53.5372037, 30.2481348 53.5371584, 30.2482096 53.5371299, 30.2483077 53.5371196, 30.2484011 53.537126, 30.2484888 53.537145, 30.2485742 53.5371835, 30.2486336 53.5372353, 30.2486768 53.5372913, 30.2487327 53.5373963, 30.2487727 53.5374399, 30.2488243 53.5374765, 30.2489 53.5374971, 30.2489804 53.5374968, 30.2490644 53.5374783, 30.2491391 53.5374385, 30.2492044 53.5373779, 30.2492354 53.5373201, 30.2492454 53.5372609, 30.2492375 53.5372061, 30.2492063 53.5371318, 30.2491147 53.5369403, 30.2491011 53.5368859, 30.2491061 53.5368371, 30.2491327 53.5367993, 30.2491896 53.536774, 30.2492567 53.5367631, 30.249337 53.5367619, 30.2494196 53.5367731, 30.2494996 53.5367778, 30.2495704 53.5367697, 30.2496426 53.5367477, 30.2496976 53.5367183, 30.2497555 53.5366938, 30.2498204 53.5366763, 30.2498945 53.5366517, 30.2499456 53.5366241, 30.2499779 53.5365938, 30.249988 53.5365632, 30.2499746 53.5365258, 30.2499358 53.5364903, 30.2498803 53.5364574, 30.2497444 53.5363719, 30.2496955 53.5363297, 30.2496617 53.5362892, 30.2496458 53.5362494, 30.2496425 53.5362089, 30.2496557 53.5361758, 30.2496885 53.5361477, 30.2497399 53.5361301, 30.2498085 53.5361188, 30.249971 53.5361096, 30.2500682 53.5360965, 30.2501832 53.5360736, 30.2503143 53.5360448, 30.2504526 53.5360247, 30.2505472 53.5360157, 30.2506448 53.5360114, 30.250745 53.5360177, 30.2508451 53.5360311, 30.2509309 53.5360435, 30.2509953 53.536063, 30.2510396 53.5360913, 30.2510791 53.5361357, 30.2510972 53.5361857, 30.2510965 53.5362412, 30.2510902 53.5363505, 30.2511075 53.5364112, 30.2511513 53.536466, 30.2512228 53.5365062, 30.2513208 53.5365391, 30.2514373 53.5365607, 30.2515904 53.5365774, 30.251678 53.5365792, 30.2517605 53.5365693, 30.2518464 53.5365437, 30.2519201 53.5365025, 30.2519892 53.5364462, 30.2520391 53.5363835, 30.2522691 53.5360212, 30.2523182 53.5359635, 30.2523794 53.5359176, 30.252446 53.5358802, 30.2525309 53.5358548, 30.2526164 53.5358395, 30.252706 53.5358415, 30.2527975 53.5358592, 30.252869 53.5358914, 30.2529466 53.5359368, 30.2530316 53.5359906, 30.2530929 53.536045, 30.2531405 53.5361085, 30.2531678 53.5361728, 30.2533307 53.5365074, 30.2533634 53.5365623, 30.2534122 53.5366044, 30.2534611 53.5366339, 30.2535261 53.5366607, 30.2536095 53.5366742, 30.2536968 53.5366755, 30.2538523 53.5366713, 30.2539178 53.5366595, 30.2539669 53.5366367, 30.2539832 53.5366084, 30.2539711 53.5365798, 30.2539264 53.5365594, 30.2538695 53.536548, 30.2537984 53.5365318, 30.2537507 53.5365145, 30.2537189 53.5364859, 30.2537071 53.5364511, 30.2537093 53.5364131, 30.2537318 53.5363655, 30.2537605 53.536301, 30.2537749 53.5362299, 30.2537729 53.5361657, 30.2537489 53.5360879, 30.2537156 53.5360086, 30.2536642 53.5359316, 30.2536081 53.535868, 30.253531 53.5357698, 30.2534822 53.5356921, 30.2534591 53.5356088, 30.2534521 53.535524, 30.2534567 53.5354503, 30.2534746 53.5353856, 30.2535186 53.5353297, 30.2535873 53.5352796, 30.2536793 53.535235, 30.2537935 53.5352026, 30.2539143 53.5351803, 30.2541159 53.5351575, 30.2542119 53.5351378, 30.254294 53.5351056, 30.2543642 53.5350586, 30.2544087 53.5350047, 30.2544514 53.5349375, 30.2545227 53.5347978, 30.2545892 53.5347272, 30.2546652 53.5346625, 30.2547765 53.5346, 30.2548698 53.5345569, 30.2549317 53.5345145, 30.254982 53.5344651, 30.2550046 53.5344077, 30.254997 53.5343503, 30.2549564 53.5342906, 30.2548888 53.5341967, 30.2548606 53.5341349, 30.2548533 53.5340699, 30.2548769 53.5340114, 30.2549179 53.5339548, 30.254987 53.5338974, 30.2551583 53.5337667, 30.2552302 53.5337046, 30.2552884 53.533635, 30.2553306 53.5335585, 30.2553595 53.5334854, 30.2554824 53.5332489, 30.2555617 53.5331223, 30.2556049 53.5330506, 30.2556301 53.5329769, 30.2556375 53.5329158, 30.2556358 53.5328554, 30.2556095 53.532795, 30.2555576 53.5327323, 30.2554805 53.5326809, 30.2553835 53.5326363, 30.2552554 53.532583, 30.2551289 53.5325146, 30.2550163 53.5324386, 30.2549023 53.532341, 30.254815 53.5322485, 30.254721 53.5321426, 30.2546332 53.5320129, 30.2545589 53.5318801, 30.2545012 53.5317383, 30.2544693 53.5316263, 30.2544443 53.5314833, 30.2544377 53.5313535, 30.2544373 53.5311761, 30.2544529 53.5310295, 30.2544796 53.5309214, 30.254535 53.5307838, 30.2545713 53.5306736, 30.2545822 53.5305559, 30.2545698 53.530434, 30.2545561 53.5303069, 30.2545702 53.5302071, 30.2546142 53.5300857, 30.254689 53.5299662, 30.2547982 53.5298435, 30.2549791 53.529684, 30.2572339 53.527884, 30.2573664 53.5277875, 30.2575084 53.5276879, 30.2576442 53.5276047, 30.2578034 53.5275241, 30.2579723 53.5274526, 30.2581246 53.5274023, 30.258283 53.5273665, 30.2584451 53.5273522, 30.2586042 53.5273543, 30.2587733 53.5273782, 30.2589388 53.5274154, 30.259483 53.5275609, 30.2595931 53.52746, 30.260092 53.5272065, 30.2603307 53.5270885, 30.2609289 53.5268557, 30.2612454 53.5267473, 30.2616423 53.526674, 30.2625516 53.5265082, 30.2636164 53.5263089, 30.2650836 53.5260649, 30.2659258 53.5259023, 30.2672079 53.5255834, 30.2683264 53.5253059, 30.2686429 53.5251991, 30.268946 53.5250684, 30.26952 53.5247287, 30.270279 53.5241531, 30.2705017 53.5238996, 30.2706465 53.523646, 30.2707002 53.5233861, 30.2707162 53.523005, 30.2706653 53.5225697, 30.2705607 53.5222364, 30.2703676 53.5217517, 30.2700403 53.5212557, 30.2698499 53.5209257, 30.2697211 53.5207646, 30.2695844 53.520645, 30.2695039 53.5204887, 30.2693 53.5201602, 30.2689889 53.519752, 30.268718 53.5193485, 30.2685812 53.5190742, 30.2685061 53.5187951, 30.2684793 53.5186509, 30.2684458 53.5184698, 30.2684283 53.5183757, 30.2682701 53.5178734, 30.2680072 53.5172594, 30.2678758 53.5170122, 30.2676961 53.5168128, 30.2670872 53.5162642, 30.2666044 53.5158687, 30.2658373 53.5154461, 30.264582 53.5148193, 30.2633697 53.5141909, 30.2623209 53.5136295, 30.2616772 53.5131319, 30.2611515 53.5126215, 30.2608699 53.5121685, 30.2606741 53.511832, 30.2606338 53.5115768, 30.2606472 53.5114061, 30.2607572 53.5112035, 30.2611542 53.5107187, 30.2615619 53.5102752, 30.2621359 53.5098414, 30.2625462 53.5095574, 30.2626938 53.5094729, 30.2629432 53.5094219, 30.265097 53.5092209, 30.2661844 53.5091596, 30.2662884 53.5089571, 30.2666467 53.5082597, 30.2616064 53.5058943, 30.2600752 53.5015073, 30.2562034 53.4910178, 30.2535351 53.4912171, 30.2535644 53.495404, 30.2516039 53.496978, 30.2425496 53.4968856, 30.2366807 53.4992973, 30.2177198 53.4980766, 30.2085585 53.5029333, 30.1865971 53.5043267)))", geometryFactory))
                        .type(TOWN)
                        .boundingBox(createPolygonByText("POLYGON ((30.1770268 53.4910178, 30.1770268 53.5434002, 30.2707162 53.5434002, 30.2707162 53.4910178, 30.1770268 53.4910178))", geometryFactory))
                        .build(),
                CityEntity.builder()
                        .name("Бялынічы")
                        .geometry(createMultipolygon("MULTIPOLYGON (((29.7333783 53.9860346, 29.7332924 53.9857397, 29.732512 53.9853235, 29.7322638 53.9843875, 29.7314584 53.9836863, 29.7310731 53.9827103, 29.7307894 53.9827374, 29.7293022 53.9778415, 29.7247528 53.9793984, 29.7234823 53.978897, 29.7170268 53.9824308, 29.7152358 53.9833656, 29.7114192 53.983937, 29.710366 53.9841238, 29.6987213 53.9783541, 29.699049 53.978096, 29.6996659 53.9777048, 29.6999931 53.9774808, 29.7003566 53.9773481, 29.7013879 53.9771811, 29.7017741 53.9770549, 29.7019672 53.9769129, 29.7021872 53.9765533, 29.7025144 53.9759538, 29.7026002 53.9754837, 29.7025037 53.9749726, 29.7025144 53.9746824, 29.7026646 53.9743542, 29.7028631 53.9741586, 29.7030616 53.9740387, 29.7034532 53.9739851, 29.7039521 53.9740576, 29.7045582 53.9740797, 29.7051108 53.9740797, 29.7055373 53.9741318, 29.7057304 53.9742201, 29.7059396 53.9742643, 29.7060871 53.9742359, 29.7062159 53.9741586, 29.7062266 53.9740419, 29.7060308 53.9738936, 29.7055855 53.9736002, 29.7055158 53.9734629, 29.705548 53.9733383, 29.7057974 53.9731632, 29.7061729 53.9730464, 29.7066477 53.9730417, 29.7070768 53.9730023, 29.7073692 53.9729108, 29.7074792 53.9728003, 29.7075301 53.9726789, 29.7075221 53.9725779, 29.7074524 53.9724548, 29.7072163 53.9723476, 29.7070178 53.9723381, 29.7066423 53.9723901, 29.706409 53.9724564, 29.706063 53.9724517, 29.7056311 53.972376, 29.7054434 53.9722592, 29.7053415 53.9721172, 29.705379 53.9719374, 29.7059101 53.9715777, 29.7062749 53.9712858, 29.7065887 53.9710776, 29.7069669 53.9709482, 29.707388 53.9708567, 29.7079512 53.9707447, 29.7082919 53.9707336, 29.7084582 53.9706831, 29.7085306 53.9705916, 29.7085038 53.9704733, 29.7084314 53.9704165, 29.7082651 53.9703913, 29.708088 53.9704134, 29.7078922 53.9704654, 29.7076455 53.9704796, 29.7072753 53.9703818, 29.7069481 53.9702777, 29.7062078 53.9700757, 29.7059879 53.9699306, 29.7059101 53.9697681, 29.7059959 53.9695977, 29.7036875 53.9705403, 29.703195 53.9706699, 29.7021699 53.9714764, 29.7022496 53.971814, 29.7007889 53.9724638, 29.7004019 53.9721425, 29.7010277 53.9719828, 29.7009216 53.9718184, 29.7002207 53.9700101, 29.6986562 53.967502, 29.697869 53.9676574, 29.6942767 53.9685312, 29.6972544 53.9715137, 29.6971652 53.9718647, 29.6971671 53.9728012, 29.6972142 53.9731689, 29.6964835 53.9733931, 29.6949352 53.9736698, 29.6942287 53.9742885, 29.6959408 53.9753228, 29.6947935 53.9759591, 29.6957404 53.9764542, 29.6961738 53.9766763, 29.694805 53.9772587, 29.6934691 53.9778863, 29.6906577 53.9791349, 29.6894941 53.9797487, 29.6880764 53.9805588, 29.6872788 53.97983, 29.6869099 53.9794075, 29.687239 53.9790273, 29.6860187 53.9783993, 29.6856067 53.9787503, 29.6853234 53.9788533, 29.6837206 53.9795066, 29.6818914 53.9807689, 29.6816133 53.9818899, 29.6830757 53.9825294, 29.6849742 53.9823808, 29.685321 53.9821061, 29.6860215 53.9817235, 29.6868684 53.9822093, 29.6876155 53.9820361, 29.6876715 53.9815812, 29.6882496 53.9816618, 29.6906473 53.9827525, 29.6902899 53.9830004, 29.690914 53.9831529, 29.691424 53.9827614, 29.6920634 53.9829875, 29.6907016 53.9846009, 29.6906678 53.9858698, 29.6901868 53.9853739, 29.6886306 53.9851142, 29.6869089 53.9837063, 29.6837219 53.9836052, 29.683719 53.984533, 29.6839933 53.9850219, 29.6811892 53.9829352, 29.6808876 53.9830807, 29.6804268 53.9836383, 29.6807885 53.9843956, 29.6826982 53.9856762, 29.6839428 53.9865216, 29.6839428 53.9868322, 29.6830963 53.9869864, 29.683827 53.9894126, 29.6816039 53.9897134, 29.680046 53.9900143, 29.6802701 53.9913615, 29.6798174 53.9915044, 29.6799813 53.9917489, 29.6804586 53.9916955, 29.6807843 53.9919784, 29.6819894 53.9916569, 29.6822423 53.9919432, 29.6838516 53.9918233, 29.6842266 53.9925205, 29.6874458 53.9919621, 29.688229 53.993151, 29.6884382 53.9933245, 29.6886045 53.9938354, 29.6887976 53.9942453, 29.6886849 53.9945828, 29.688406 53.994835, 29.6876979 53.9951851, 29.6876121 53.995286, 29.6876764 53.9954153, 29.6882682 53.9962216, 29.6884344 53.9966599, 29.6892897 53.9970776, 29.6898819 53.9971432, 29.6903969 53.9969817, 29.6905878 53.9969294, 29.6913629 53.9972548, 29.6923479 53.9968419, 29.6930721 53.9969757, 29.6938352 53.9969377, 29.6946324 53.9967934, 29.695166 53.9961982, 29.6959138 53.9963528, 29.6969187 53.9964768, 29.6986786 53.9964134, 29.6978822 53.9972573, 29.6981514 54.0031533, 29.7001363 54.0038725, 29.6978641 54.0054242, 29.6961287 54.0086356, 29.6969408 54.0089131, 29.6973746 54.008596, 29.699369 54.0089145, 29.6995698 54.0096626, 29.7013778 54.0094531, 29.7026953 54.0118274, 29.7034614 54.0136796, 29.7043839 54.0153906, 29.7184539 54.012436, 29.7191572 54.0129131, 29.7218921 54.01428, 29.7245134 54.0158214, 29.7265037 54.017275, 29.726862 54.0172417, 29.7271559 54.0179024, 29.7274981 54.0198315, 29.7302514 54.0196127, 29.7311187 54.0195261, 29.7318178 54.0193543, 29.7327941 54.018787, 29.7328478 54.0182512, 29.732812 54.0174637, 29.7338818 54.0162383, 29.7340656 54.0160278, 29.7350023 54.0153648, 29.7346063 54.0151759, 29.732453 54.0133574, 29.7322945 54.0130233, 29.7327699 54.0127603, 29.7394629 54.0111937, 29.7382604 54.0098791, 29.7378689 54.0094408, 29.7354266 54.00989, 29.7349325 54.0094737, 29.7327513 54.0098736, 29.7323784 54.0094846, 29.7318098 54.00903, 29.7311386 54.0090738, 29.7306818 54.0086027, 29.7300666 54.008515, 29.7298056 54.0083233, 29.7295073 54.0080549, 29.729209 54.0078412, 29.7285133 54.0080257, 29.7281271 54.0080761, 29.7259813 54.008549, 29.7257024 54.0086057, 29.7251016 54.0087192, 29.7247904 54.008612, 29.7242472 54.0078623, 29.7245047 54.007252, 29.7265132 54.0060464, 29.7265904 54.0057084, 29.7272427 54.0055571, 29.7269681 54.0047298, 29.7253988 54.0023089, 29.7249766 54.0017295, 29.7260238 54.0015832, 29.7258896 54.0003434, 29.7256492 53.9996048, 29.726597 53.9984879, 29.72853 53.9966793, 29.7291909 53.9955138, 29.730547 53.9938992, 29.7287274 53.9933795, 29.7294141 53.991563, 29.7307788 53.9907354, 29.7308816 53.9900084, 29.7313022 53.9883835, 29.7312164 53.986481, 29.7333494 53.9864372, 29.7333783 53.9860346)), ((29.7623358 53.9971259, 29.762883 53.9965331, 29.7615311 53.9958772, 29.760194 53.9968656, 29.7583385 53.9966637, 29.757867 53.9952336, 29.7573832 53.9950711, 29.7541712 53.9959655, 29.7541497 53.9964763, 29.7476266 53.9964826, 29.7474978 53.9981728, 29.7490964 53.9982926, 29.7492144 53.9987593, 29.7516041 53.9990013, 29.7517679 53.9990179, 29.7551689 53.9980467, 29.7567139 53.9983683, 29.7590635 53.9983872, 29.7603295 53.9996233, 29.7615097 53.9996422, 29.7618423 53.9987782, 29.7628465 53.9992058, 29.7639795 53.998111, 29.7623358 53.9971259)))", geometryFactory))
                        .type(TOWN)
                        .boundingBox(createPolygonByText("POLYGON ((29.6798174 53.967502, 29.6798174 54.0198315, 29.7639795 54.0198315, 29.7639795 53.967502, 29.6798174 53.967502))", geometryFactory))
                        .build(),
                CityEntity.builder()
                        .name("Кіраўск")
                        .geometry(createMultipolygon("MULTIPOLYGON (((29.4622316 53.2594419, 29.461396 53.2587834, 29.4607382 53.2590114, 29.4594782 53.2587649, 29.4580945 53.2595148, 29.4595435 53.2606358, 29.4594406 53.2607497, 29.4591412 53.2613, 29.4587382 53.2622463, 29.4586052 53.2625551, 29.4584401 53.2628125, 29.45826 53.2632824, 29.4581544 53.2635791, 29.4578654 53.2642038, 29.4577373 53.2645526, 29.4571932 53.2657585, 29.45705 53.2659154, 29.4570197 53.2663846, 29.4569026 53.2665342, 29.4568499 53.2666356, 29.4567349 53.266734, 29.4561734 53.2670443, 29.4557944 53.2673033, 29.4552508 53.2675838, 29.4538632 53.2683759, 29.4536389 53.2684851, 29.453377 53.2686359, 29.4532718 53.2688052, 29.4531023 53.2690451, 29.452989 53.2692759, 29.4524895 53.2699751, 29.4523516 53.2700733, 29.4522952 53.2701996, 29.4521294 53.2703467, 29.4517974 53.2707087, 29.4517282 53.2710201, 29.4517158 53.2713351, 29.4517714 53.2716198, 29.4517947 53.2719943, 29.4518351 53.2727944, 29.4519398 53.2729112, 29.4519031 53.2730236, 29.4515698 53.273281, 29.4511882 53.2735095, 29.4509615 53.2737612, 29.4508103 53.2738248, 29.4499081 53.274453, 29.4497596 53.2746225, 29.4491175 53.2751385, 29.4487991 53.2753285, 29.4486283 53.2754746, 29.4483288 53.2756913, 29.4480408 53.2758873, 29.4478078 53.2761192, 29.4476338 53.2761907, 29.4474743 53.2763291, 29.4468096 53.2768256, 29.446263 53.2772708, 29.446049 53.27753, 29.4457813 53.2779431, 29.445482 53.2782882, 29.4451033 53.2786031, 29.4447419 53.2789089, 29.4460978 53.2795232, 29.4464473 53.2809419, 29.445389 53.2824036, 29.4436583 53.283672, 29.4424935 53.2843145, 29.4409987 53.2851494, 29.4425678 53.2860007, 29.4435563 53.286537, 29.4433671 53.2867552, 29.4447317 53.2872536, 29.4459761 53.2864563, 29.4502204 53.2830389, 29.4508077 53.2826571, 29.4514564 53.2828798, 29.4517396 53.2827233, 29.4530821 53.2832898, 29.4531096 53.2832728, 29.4534284 53.2830761, 29.4544068 53.2834494, 29.4580946 53.2800748, 29.4600258 53.281146, 29.4631586 53.2826791, 29.4667521 53.2826336, 29.467545 53.2874929, 29.4689196 53.2874252, 29.4714766 53.2871339, 29.4724288 53.2887609, 29.4730725 53.2886686, 29.4741363 53.2905963, 29.4743072 53.2905652, 29.4750534 53.2904294, 29.4760218 53.2922211, 29.4746504 53.2923391, 29.4736197 53.2926205, 29.4731795 53.2927407, 29.4733626 53.2929976, 29.4727671 53.293145, 29.4716599 53.2934191, 29.471955 53.2938516, 29.4713074 53.2940512, 29.470442 53.2944963, 29.4705493 53.2945707, 29.4712188 53.2950068, 29.4728754 53.2945707, 29.4733689 53.2943783, 29.4739798 53.2942421, 29.4805316 53.2927815, 29.4801538 53.2922294, 29.4842685 53.2915147, 29.4842987 53.2918584, 29.4844144 53.2920636, 29.4874442 53.2915634, 29.4871867 53.2910067, 29.4857363 53.2881835, 29.4848944 53.2869549, 29.4828396 53.287174, 29.4824939 53.2852901, 29.4812531 53.2853687, 29.4810623 53.2839574, 29.480956 53.2831714, 29.4808543 53.2815331, 29.4808474 53.2814222, 29.4810213 53.2814043, 29.4813134 53.2813743, 29.4836934 53.2809146, 29.4832734 53.2801516, 29.4850552 53.2797472, 29.4854947 53.2796941, 29.4865853 53.2794, 29.4876947 53.2803256, 29.4879893 53.2805489, 29.4880703 53.2805179, 29.4885348 53.2809078, 29.4890755 53.2806522, 29.4882971 53.2800329, 29.4885507 53.2799305, 29.4891072 53.2797058, 29.4895107 53.27992, 29.4902138 53.2795809, 29.490115 53.2792336, 29.4907286 53.2789507, 29.4858766 53.2755296, 29.4860817 53.275414, 29.4874679 53.2745415, 29.4884464 53.2738948, 29.4898239 53.2728298, 29.4911114 53.2719931, 29.4922272 53.2711334, 29.4923969 53.2710408, 29.492126 53.2708429, 29.4953704 53.2692748, 29.4964354 53.2686179, 29.4959324 53.2680365, 29.4946862 53.2672546, 29.492382 53.2659201, 29.491234 53.2653001, 29.4892812 53.2641248, 29.4873179 53.2641704, 29.4872299 53.263853, 29.4870153 53.2636938, 29.4884637 53.2627774, 29.4883114 53.2626491, 29.4915257 53.2606776, 29.4909422 53.260121, 29.4917276 53.2594901, 29.493753 53.2579656, 29.4921737 53.2579334, 29.4840955 53.2577687, 29.4840475 53.2574447, 29.4833982 53.2574336, 29.4834591 53.2558187, 29.4823103 53.2547682, 29.4808474 53.2547307, 29.4806644 53.2546857, 29.4804172 53.2546676, 29.479768 53.2553344, 29.4788885 53.25621, 29.4787145 53.2566025, 29.478642 53.2574225, 29.4784146 53.2591265, 29.4786393 53.2609321, 29.4785315 53.2610787, 29.4781525 53.2614419, 29.4776347 53.2616022, 29.4757793 53.2619565, 29.472944 53.2613095, 29.4707726 53.2605513, 29.4686431 53.2594465, 29.4684535 53.2595626, 29.4681861 53.2596637, 29.4677042 53.2599509, 29.467164 53.2602205, 29.4670217 53.2603334, 29.4666391 53.2604316, 29.4663274 53.2604687, 29.4650014 53.2599861, 29.464407 53.2598253, 29.4634059 53.2595147, 29.4632294 53.2594807, 29.4630787 53.2594317, 29.4629499 53.2593113, 29.4628024 53.2592937, 29.4627179 53.2592568, 29.4626173 53.259264, 29.4625275 53.2593426, 29.4623598 53.2594005, 29.4622316 53.2594419)))", geometryFactory))
                        .type(TOWN)
                        .boundingBox(createPolygonByText("POLYGON ((29.4409987 53.2546676, 29.4409987 53.2950068, 29.4964354 53.2950068, 29.4964354 53.2546676, 29.4409987 53.2546676))", geometryFactory))
                        .build(),
                CityEntity.builder()
                        .name("Клічаў")
                        .geometry(createMultipolygon("MULTIPOLYGON (((29.3396398 53.5031338, 29.3539998 53.4875101, 29.3559905 53.4882628, 29.3603894 53.4901651, 29.3630594 53.4930508, 29.3619991 53.4935145, 29.362929 53.4947102, 29.3645356 53.4943927, 29.3618738 53.4908277, 29.3714286 53.4913855, 29.371783 53.4901486, 29.3698662 53.4901143, 29.3699246 53.4892885, 29.368561 53.4892373, 29.3683445 53.487407, 29.3614415 53.4875774, 29.3576927 53.4882555, 29.3543952 53.4865855, 29.3568519 53.4846374, 29.3562469 53.4828087, 29.3557563 53.479957, 29.3569412 53.4769227, 29.3603985 53.4765646, 29.3595746 53.4743986, 29.3564589 53.473147, 29.3533862 53.470516, 29.3475396 53.4677542, 29.3425271 53.4663345, 29.3356134 53.4659813, 29.3289734 53.4677415, 29.3244018 53.4704602, 29.323354 53.4728661, 29.3173287 53.474511, 29.3109772 53.4758341, 29.3070941 53.481971, 29.3089498 53.4906311, 29.3125222 53.4906226, 29.3131745 53.4948558, 29.3203929 53.5012991, 29.3191011 53.5037061, 29.3228991 53.5048113, 29.3219013 53.5052618, 29.320174 53.505355, 29.3202555 53.5064908, 29.3253493 53.5076375, 29.3244698 53.511368, 29.328006 53.5135831, 29.3330443 53.5128634, 29.3340013 53.514734, 29.3358939 53.5143563, 29.3348016 53.5113782, 29.3396398 53.5031338)))", geometryFactory))
                        .type(TOWN)
                        .boundingBox(createPolygonByText("POLYGON ((29.3070941 53.4659813, 29.3070941 53.514734, 29.371783 53.514734, 29.371783 53.4659813, 29.3070941 53.4659813))", geometryFactory))
                        .build()
        );
    }
}
