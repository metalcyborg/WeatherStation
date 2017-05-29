package com.metalcyborg.weather.citylist;

import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.metalcyborg.weather.Injection;
import com.metalcyborg.weather.R;
import com.metalcyborg.weather.citysearch.CityAdapter;
import com.metalcyborg.weather.data.source.WeatherRepository;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.longClick;
import static android.support.test.espresso.action.ViewActions.typeTextIntoFocusedView;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.google.common.base.Preconditions.checkArgument;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.core.IsNot.not;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class CityListScreenTest {

    private static final String TEST_CITY_NAME = "Tyumen";

    public static Matcher<RecyclerView.ViewHolder> withHolderCityNameView(final String cityName) {
        return new BoundedMatcher<RecyclerView.ViewHolder, CityAdapter.CityViewHolder>(
                CityAdapter.CityViewHolder.class) {
            @Override
            public void describeTo(Description description) {
                description.appendText("No ViewHolder found with city name: " + cityName);
            }

            @Override
            protected boolean matchesSafely(CityAdapter.CityViewHolder item) {
                TextView cityNameTextView = (TextView) item.itemView.findViewById(R.id.cityName);
                if(cityName == null) {
                    return false;
                }
                return cityNameTextView.getText().toString().equals(cityName);
            }
        };
    }

    private Matcher<View> withItemText(final String itemText) {
        checkArgument(!TextUtils.isEmpty(itemText), "itemText cannot be null or empty");
        return new TypeSafeMatcher<View>() {
            @Override
            public boolean matchesSafely(View item) {
                return allOf(
                        isDescendantOfA(isAssignableFrom(RecyclerView.class)),
                        withText(itemText)).matches(item);
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("is isDescendantOfA RV with text " + itemText);
            }
        };
    }

    @Rule
    public ActivityTestRule<CityListActivity> mCityListActivityTestRule =
            new ActivityTestRule<CityListActivity>(CityListActivity.class){
                @Override
                protected void beforeActivityLaunched() {
                    super.beforeActivityLaunched();

                    // Delete test city from city list
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
    public void clickRecyclerViewItem_showForecast() {

    }

    @Test
    public void addCityToList() {
        addNewCity(TEST_CITY_NAME);
        // Find test city name in recyclerView on the CityListActivity
        onView(withItemText(TEST_CITY_NAME)).check(matches(isDisplayed()));
    }

    @Test
    public void deleteCityFromList() {
        // Add city
        addNewCity(TEST_CITY_NAME);
        // Start action mode
        onView(withId(R.id.recycler)).perform(RecyclerViewActions.actionOnItemAtPosition(0,
                longClick()));
        // Click delete button
        onView(withId(R.id.delete)).perform(click());
        // Verify that test city is not shown in the list
        onView(withItemText(TEST_CITY_NAME)).check(doesNotExist());
    }

    private void addNewCity(String cityName) {
        // Click fab button
        onView(withId(R.id.fab)).perform(click());
        // Type test city name
        onView(withId(R.id.search)).perform(typeTextIntoFocusedView(cityName));
        // Find test city name in search recyclerView and click that item
        onView(withId(R.id.cityRecycler))
                .perform(RecyclerViewActions
                        .actionOnHolderItem(withHolderCityNameView(cityName), click()));
    }
}
