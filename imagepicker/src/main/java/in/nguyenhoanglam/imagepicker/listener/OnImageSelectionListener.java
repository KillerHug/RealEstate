package in.nguyenhoanglam.imagepicker.listener;

import in.nguyenhoanglam.imagepicker.model.Image;

import java.util.List;

/**
 * Created by hoanglam on 8/18/17.
 */

public interface OnImageSelectionListener {
    void onSelectionUpdate(List<Image> images);
}
