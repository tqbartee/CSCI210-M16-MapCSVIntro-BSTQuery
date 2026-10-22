import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class treesappTest {

    private SimpleMapBST<StringInt, String> map;

    private static final String EXPECTED_ELTON_JOHN = String.join("\n",
            "Line 10: Candle in the Wind '97",
            "Line 221: Crocodile Rock",
            "Line 479: Something About the Way You look Tonight",
            "Line 520: Goodbye Yellow Brick Road",
            "Line 552: Can You Feel the Love Tonight",
            "Line 602: Nikita",
            "Line 958: Philadelphia Freedom",
            "Line 1091: Daniel",
            "Line 1353: Your Song",
            "Line 1377: Bennie & the Jets",
            "Line 1462: The One",
            "Line 1719: Rocket Man",
            "Line 1760: Believe",
            "Line 1841: Little Jeannie",
            "Line 1857: Sacrifice",
            "Line 1878: Island Girl",
            "Line 1911: Lucy in the Sky With Diamonds",
            "Line 2068: Don't Let the Sun Go Down On Me",
            "Line 2356: I Don't Wanna Go On With You Like That",
            "Line 2486: I'm Still Standing",
            "Line 2691: Circle of Life",
            "Line 3081: Someone Saved My Life Tonight",
            "Line 3401: You Gotta Love Someone",
            "Line 3696: I Guess That's Why They Call it the Blues",
            "Line 3705: The Bitch is Back",
            "Line 3866: Sorry Seems to Be the Hardest Word",
            "Line 3971: Blue Eyes",
            "Line 4615: Sad Songs (Say So Much)",
            "Line 4843: Empty Garden (Hey Hey Johnny)"
    );

    private static final String EXPECTED_MICHAEL_JACKSON = String.join("\n",
            "Line 28: Billie Jean",
            "Line 128: Black Or White",
            "Line 148: Beat It",
            "Line 234: You Are Not Alone",
            "Line 419: Don't Stop 'Til You Get Enough",
            "Line 527: Thriller",
            "Line 872: Bad",
            "Line 896: Remember the Time",
            "Line 1098: Ben",
            "Line 1099: Blood on the Dance Floor",
            "Line 1233: Earth Song",
            "Line 1269: The Way You Make Me Feel",
            "Line 1345: Dirty Diana",
            "Line 1362: They Don't Care About Us",
            "Line 1461: You Rock My World",
            "Line 1659: Man in the Mirror",
            "Line 1686: Rock With You",
            "Line 2134: Heal the World",
            "Line 2257: Wanna Be Startin' Something",
            "Line 2333: Smooth Criminal",
            "Line 2559: One Day in Your Life",
            "Line 2602: Will You Be There?",
            "Line 3337: In the Closet",
            "Line 3635: Off the Wall",
            "Line 3795: Got to Be There",
            "Line 4172: Give in to Me",
            "Line 4650: Leave Me Alone",
            "Line 4664: Stranger in Moscow",
            "Line 4770: One More Chance",
            "Line 4804: Who is It"
    );

    @BeforeEach
    void setUp() {
        map = new SimpleMapBST<>();
        SimpleMapBSTBuilder.readGenericMapFromCSV(map, "Top5000SongsEdited.csv");
    }

    @Test
    void testQueryEltonJohn() {
        String actual = map.get(new StringInt("Elton John"));
        assertNotNull(actual, "Query for 'Elton John' should not return null");
        assertEquals(EXPECTED_ELTON_JOHN, actual, "Returned songs for Elton John do not match expected value");
    }

    @Test
    void testQueryMichaelJackson() {
        String actual = map.get(new StringInt("Michael Jackson"));
        assertNotNull(actual, "Query for 'Michael Jackson' should not return null");
        assertEquals(EXPECTED_MICHAEL_JACKSON, actual, "Returned songs for Michael Jackson do not match expected value");
    }
}
