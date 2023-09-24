package in.nguyenhoanglam.imagepicker.ui.camera;

import in.nguyenhoanglam.imagepicker.model.Image;
import in.nguyenhoanglam.imagepicker.ui.common.MvpView;

import java.util.List;

/**
 * Created by hoanglam on 8/22/17.
 */

public interface CameraView extends MvpView {

    void finishPickImages(List<Image> images);
}
