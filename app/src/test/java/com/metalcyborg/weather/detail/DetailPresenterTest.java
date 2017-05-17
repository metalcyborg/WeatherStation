package com.metalcyborg.weather.detail;

import com.metalcyborg.weather.data.source.WeatherDataSource;

import org.junit.Before;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.when;

public class DetailPresenterTest {

    private DetailPresenter mPresenter;

    @Mock
    private WeatherDataSource mRepository;

    @Mock
    private DetailContract.View mView;

    @Before
    public void setupPresenter() {
        MockitoAnnotations.initMocks(this);

        mPresenter = new DetailPresenter(mRepository, mView);
        when(mView.isActive()).thenReturn(true);
    }


}
