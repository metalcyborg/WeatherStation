package com.metalcyborg.weather.citylist;

import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.ViewAssertion;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.metalcyborg.weather.Injection;
import com.metalcyborg.weather.R;
import com.metalcyborg.weather.TestUtils;
import com.metalcyborg.weather.citysearch.CityAdapter;
import com.metalcyborg.weather.data.source.WeatherRepository;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.longClick;
import static android.support.test.espresso.action.ViewActions.typeTextIntoFocusedView;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.assertThat;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.google.common.base.Preconditions.checkArgument;
import static junit.framework.Assert.fail;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class CityListScreenTest {

    private static final String TEST_CITY_NAME = "Tyumen";
    private static final String TEST_COUNTRY_NAME = "RU";
    private static final String TEST_CITY_NAME_2 = "Moscow";
    private static final String TEST_COUNTRY_NAME_2 = "RU";

    private static ViewInteraction matchToolbarTitle(
            CharSequence title) {
        return onView(
                allOf(
                        isAssignableFrom(TextView.class),
                        withParent(isAssignableFrom(Toolbar.class))))
                .check(matches(withText(title.toString())));
    }

    @Rule
    public ActivityTestRule<CityListActivity> mCityListActivityTestRule =
            new ActivityTestRule<CityListActivity>(CityListActivity.class){
                @Override
                protected void beforeActivityLaunched() {
                    super.beforeActivityLaunched();

                    // Delete all cities
                    WeatherRepository repository = Injection
                            .provideWeatherRepository(InstrumentationRegistry.getTargetContext());
                    repository.deleteAllDataFromCityAndWeatherLists();
                }
            };

    @Test
    public void clickFab_showSearchActivity() {
        // Click fab (add new city)
        onView(withId(R.id.fab)).perform(click());
        // Check if the city search recyclerView is displayed
        onView(withId(R.id.cityRecycler)).check(matches(isDisplayed()));
    }

    @Test
    public void addCityToList() {
        addNewCity(TEST_CITY_NAME, TEST_COUNTRY_NAME);
        // Find test city name in recyclerView on the CityListActivity
        onView(TestUtils.withItemText(TEST_CITY_NAME)).check(matches(isDisplayed()));
    }

    @Test
    public void addOneCity_deleteOneCity() {
        // Add city
        addNewCity(TEST_CITY_NAME, TEST_COUNTRY_NAME);
        // Start action mode
        onView(withId(R.id.recycler)).perform(RecyclerViewActions.actionOnItemAtPosition(0,
                longClick()));
        // Click delete button
        onView(withId(R.id.delete)).perform(click());
        // Verify that test city is not shown in the list
        onView(TestUtils.withItemText(TEST_CITY_NAME)).check(doesNotExist());
    }

    @Test
    public void addTwoCities_deleteOneCity() {
        // Add cities
        addNewCity(TEST_CITY_NAME, TEST_COUNTRY_NAME);
        addNewCity(TEST_CITY_NAME_2, TEST_COUNTRY_NAME_2);
        // Start action mode
        onView(withId(R.id.recycler)).perform(RecyclerViewActions.actionOnItemAtPosition(0,
                longClick()));
        // Click delete button
        onView(withId(R.id.delete)).perform(click());
        // Verify that only one city was deleted
        onView(TestUtils.withItemText(TEST_CITY_NAME)).check(doesNotExist());
        onView(TestUtils.withItemText(TEST_CITY_NAME_2)).check(matches(isDisplayed()));
    }

    @Test
    public void openSettingsActivity() {
        // Open menu
        openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getTargetContext());
        onView(withText(R.string.settings)).perform(click());
        // Verify that settings activity is shown
        onView(withText(R.string.pref_title_temperature)).check(matches(isDisplayed()));
    }

    @Test
    public void clickCityItem_showDetails() {
        // Add one city
        addNewCity(TEST_CITY_NAME, TEST_COUNTRY_NAME);
        // Click first item
        onView(withId(R.id.recycler)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        // Verify that DetailsActivity is shown
        onView(withText(R.string.details_header)).check(matches(isDisplayed()));
        // Verify that header has a test city name
        matchToolbarTitle(TEST_CITY_NAME);
    }

    private void addNewCity(String cityName, String countryName) {
        // Click fab button
        onView(withId(R.id.fab)).perform(click());
        // Type test city name
        onView(withId(R.id.search)).perform(typeTextIntoFocusedView(cityName));
        // Find test city name in search recyclerView and click that item
        onView(withId(R.id.cityRecycler))
                .perform(RecyclerViewActions
                        .actionOnHolderItem(allOf(
                                TestUtils.withHolderCityNameView(cityName),
                                TestUtils.withHolderCountryNameView(countryName)), click()));
    }
}
