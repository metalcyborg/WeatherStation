package com.metalcyborg.weather;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.ViewAssertion;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.metalcyborg.weather.citysearch.CityAdapter;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import static android.support.test.espresso.matcher.ViewMatchers.assertThat;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.google.common.base.Preconditions.checkArgument;
import static junit.framework.Assert.fail;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.core.Is.is;

public class TestUtils {

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
                if (cityName == null) {
                    return false;
                }
                return cityNameTextView.getText().toString().equals(cityName);
            }
        };
    }

    public static Matcher<RecyclerView.ViewHolder> withHolderCountryNameView(final String countryName) {
        return new BoundedMatcher<RecyclerView.ViewHolder, CityAdapter.CityViewHolder>(
                CityAdapter.CityViewHolder.class) {
            @Override
            public void describeTo(Description description) {
                description.appendText("No ViewHolder found with country name: " + countryName);
            }

            @Override
            protected boolean matchesSafely(CityAdapter.CityViewHolder item) {
                TextView countryNameTextView = (TextView) item.itemView.findViewById(R.id.countryName);
                if (countryName == null) {
                    return false;
                }
                return countryNameTextView.getText().toString().equals(countryName);
            }
        };
    }

    public static Matcher<View> withItemText(final String itemText) {
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

    public static class RecyclerViewMatcher {
        private final int recyclerViewId;

        public RecyclerViewMatcher(int recyclerViewId) {
            this.recyclerViewId = recyclerViewId;
        }

        public Matcher<View> atPosition(final int position) {
            return atPositionOnView(position, -1);
        }

        public Matcher<View> atPositionOnView(final int position, final int targetViewId) {

            return new TypeSafeMatcher<View>() {
                Resources resources = null;
                View childView;

                public void describeTo(Description description) {
                    String idDescription = Integer.toString(recyclerViewId);
                    if (this.resources != null) {
                        try {
                            idDescription = this.resources.getResourceName(recyclerViewId);
                        } catch (Resources.NotFoundException var4) {
                            idDescription = String.format("%s (resource name not found)",
                                    new Object[]{Integer.valueOf
                                            (recyclerViewId)});
                        }
                    }

                    description.appendText("with id: " + idDescription);
                }

                public boolean matchesSafely(View view) {

                    this.resources = view.getResources();

                    if (childView == null) {
                        RecyclerView recyclerView =
                                (RecyclerView) view.getRootView().findViewById(recyclerViewId);
                        if (recyclerView != null && recyclerView.getId() == recyclerViewId) {
                            childView = recyclerView.getChildAt(position);
                        } else {
                            return false;
                        }
                    }

                    if (targetViewId == -1) {
                        return view == childView;
                    } else {
                        View targetView = childView.findViewById(targetViewId);
                        return view == targetView;
                    }

                }
            };
        }
    }

    public static RecyclerViewMatcher withRecyclerView(final int recyclerViewId) {
        return new RecyclerViewMatcher(recyclerViewId);
    }

    public static class RecyclerViewItemCountAssertion implements ViewAssertion {

        private int mExpectedCount;

        public RecyclerViewItemCountAssertion(int expectedCount) {
            mExpectedCount = expectedCount;
        }

        @Override
        public void check(View view, NoMatchingViewException noViewFoundException) {
            if (noViewFoundException != null) {
                throw noViewFoundException;
            }

            if (!(view instanceof RecyclerView)) {
                fail("View is not instance of RecyclerView");
            }

            RecyclerView.Adapter adapter = ((RecyclerView) view).getAdapter();
            assertThat(adapter.getItemCount(), is(mExpectedCount));
        }
    }

    public static ViewAssertion hasItemCount(final int count) {
        return new RecyclerViewItemCountAssertion(count);
    }

    public static class ImageMatcher extends TypeSafeMatcher<View> {

        private int mDrawableId;

        public ImageMatcher(int drawableId) {
            mDrawableId = drawableId;
        }

        @Override
        protected boolean matchesSafely(View item) {
            if (!(item instanceof ImageView))
                return false;

            ImageView target = (ImageView) item;
            BitmapDrawable bmd = (BitmapDrawable) target.getDrawable();
            Bitmap targetBitmap = bmd.getBitmap();

            Resources resources = item.getContext().getResources();
            BitmapDrawable expectedDrawable = (BitmapDrawable) resources.getDrawable(mDrawableId, null);
            Bitmap expected = expectedDrawable.getBitmap();

            return expected.sameAs(targetBitmap);
        }

        @Override
        public void describeTo(Description description) {
            description.appendText("with drawable from resource id: ");
            description.appendValue(mDrawableId);
        }
    }

    public static TypeSafeMatcher<View> withImage(int resourceId) {
        return new ImageMatcher(resourceId);
    }
}
