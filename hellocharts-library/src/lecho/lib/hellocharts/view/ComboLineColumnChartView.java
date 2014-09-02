package lecho.lib.hellocharts.view;

import lecho.lib.hellocharts.BuildConfig;
import lecho.lib.hellocharts.ColumnChartDataProvider;
import lecho.lib.hellocharts.ComboLineColumnChartDataProvider;
import lecho.lib.hellocharts.LineChartDataProvider;
import lecho.lib.hellocharts.model.ChartData;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.ColumnValue;
import lecho.lib.hellocharts.model.ComboLineColumnChartData;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.SelectedValue;
import lecho.lib.hellocharts.renderer.ComboLineColumnChartRenderer;
import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;

public class ComboLineColumnChartView extends AbstractChartView implements ComboLineColumnChartDataProvider {
	private static final String TAG = "ComboLineColumnChartView";
	protected ComboLineColumnChartData data;
	protected ColumnChartDataProvider columnChartDataProvider = new ComboColumnChartDataProvider();
	protected LineChartDataProvider lineChartDataProvider = new ComboLineChartDataProvider();
	protected ComboLineColumnChartOnValueTouchListener onValueTouchListener = new DummyOnValueTouchListener();

	public ComboLineColumnChartView(Context context) {
		this(context, null, 0);
	}

	public ComboLineColumnChartView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public ComboLineColumnChartView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		chartRenderer = new ComboLineColumnChartRenderer(context, this, columnChartDataProvider, lineChartDataProvider);
		setComboLineColumnChartData(ComboLineColumnChartData.generateDummyData());
	}

	@Override
	public ComboLineColumnChartData getComboLineColumnChartData() {
		return data;
	}

	@Override
	public void setComboLineColumnChartData(ComboLineColumnChartData data) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "Setting data for ComboLineColumnChartView");
		}

		if (null == data) {
			this.data = null;// generateDummyData();
		} else {
			this.data = data;
		}

		axesRenderer.initAxesMeasurements();
		chartRenderer.initMaxViewport();
		chartRenderer.initCurrentViewport();
		chartRenderer.initDataMeasuremetns();

		ViewCompat.postInvalidateOnAnimation(ComboLineColumnChartView.this);
	}

	@Override
	public ChartData getChartData() {
		return data;
	}

	@Override
	public void callChartTouchListener(SelectedValue selectedValue) {
		if (ComboLineColumnChartRenderer.TYPE_COLUMN == selectedValue.getThirdIndex()) {
			ColumnValue value = data.getColumnChartData().getColumns().get(selectedValue.getFirstIndex()).getValues()
					.get(selectedValue.getSecondIndex());
			onValueTouchListener.onValueTouched(selectedValue.getFirstIndex(), selectedValue.getSecondIndex(), value,
					null);
		} else if (ComboLineColumnChartRenderer.TYPE_LINE == selectedValue.getThirdIndex()) {
			PointValue value = data.getLineChartData().getLines().get(selectedValue.getFirstIndex()).getValues()
					.get(selectedValue.getSecondIndex());
			onValueTouchListener.onValueTouched(selectedValue.getFirstIndex(), selectedValue.getSecondIndex(), null,
					value);
		} else {
			throw new IllegalArgumentException("Invalid selected value type " + selectedValue.getThirdIndex());
		}
	}

	public ComboLineColumnChartOnValueTouchListener getOnValueTouchListener() {
		return onValueTouchListener;
	}

	public void setOnValueTouchListener(ComboLineColumnChartOnValueTouchListener touchListener) {
		if (null == touchListener) {
			this.onValueTouchListener = new DummyOnValueTouchListener();
		} else {
			this.onValueTouchListener = touchListener;
		}
	}

	public interface ComboLineColumnChartOnValueTouchListener {
		public void onValueTouched(int selectedLine, int selectedValue, ColumnValue columnValue, PointValue pointValue);
	}

	private static class DummyOnValueTouchListener implements ComboLineColumnChartOnValueTouchListener {

		@Override
		public void onValueTouched(int selectedLine, int selectedValue, ColumnValue columnValue, PointValue pointValue) {
			// TODO Auto-generated method stub

		}
	}

	private class ComboLineChartDataProvider implements LineChartDataProvider {

		@Override
		public LineChartData getLineChartData() {
			return ComboLineColumnChartView.this.data.getLineChartData();
		}

		@Override
		public void setLineChartData(LineChartData data) {
			ComboLineColumnChartView.this.data.setLineChartData(data);

		}

	}

	private class ComboColumnChartDataProvider implements ColumnChartDataProvider {

		@Override
		public ColumnChartData getColumnChartData() {
			return ComboLineColumnChartView.this.data.getColumnChartData();
		}

		@Override
		public void setColumnChartData(ColumnChartData data) {
			ComboLineColumnChartView.this.data.setColumnChartData(data);

		}

	}

}