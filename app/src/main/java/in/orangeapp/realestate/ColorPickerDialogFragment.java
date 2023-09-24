package in.orangeapp.realestate;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.preference.PreferenceManager;

import com.jaredrummler.android.colorpicker.ColorPanelView;
import com.jaredrummler.android.colorpicker.ColorPickerView;


public class ColorPickerDialogFragment extends DialogFragment implements ColorPickerView.OnColorChangedListener, View.OnClickListener {

    OnColorPickerListener onColorPickerListener;
    Context context;
    private ColorPickerView colorPickerView;
    private ColorPanelView newColorPanelView;
    public ColorPickerDialogFragment(CreateAdvertisementActivity createAdvertisementActivity) {
        context = createAdvertisementActivity;
        onColorPickerListener = (OnColorPickerListener) context;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Dialog dialog = new Dialog(requireContext(), R.style.DialogFragmentTheme);
        dialog.setContentView(R.layout.dialog_fragment_color_picker);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(dialog.getWindow().getAttributes());
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        dialog.getWindow().setAttributes(layoutParams);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        int initialColor = prefs.getInt("color_3", 0xFF000000);

        colorPickerView = (ColorPickerView) dialog.findViewById(R.id.cpv_color_picker_view);
        ColorPanelView colorPanelView = (ColorPanelView) dialog.findViewById(R.id.cpv_color_panel_old);
        newColorPanelView = (ColorPanelView) dialog.findViewById(R.id.cpv_color_panel_new);

        TextView btnOK = (TextView) dialog.findViewById(R.id.okButton);
        TextView btnCancel = (TextView) dialog.findViewById(R.id.cancelButton);

        ((LinearLayout) colorPanelView.getParent()).setPadding(colorPickerView.getPaddingLeft(), 0,
                colorPickerView.getPaddingRight(), 0);

        colorPickerView.setOnColorChangedListener(this);
        colorPickerView.setColor(initialColor, true);
        colorPanelView.setColor(initialColor);

        btnOK.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        return dialog;
    }


    @Override public void onColorChanged(int newColor) {
        newColorPanelView.setColor(colorPickerView.getColor());
    }

    @Override public void onClick(View v) {
        switch (v.getId()) {
            case R.id.okButton:
                /*SharedPreferences.Editor edit = PreferenceManager.getDefaultSharedPreferences(context).edit();
                edit.putInt("color_3", colorPickerView.getColor());
                edit.apply();*/
                onColorPickerListener.onColorSelected(colorPickerView.getColor());
                dismiss();
                break;
            case R.id.cancelButton:
                dismiss();
                break;
        }
    }

    interface OnColorPickerListener {
        void onColorSelected(int color);
    }
}
