package yml;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.utils.Disposable;

import java.util.List;

public class Box implements Disposable{
    public String id;
    public List position,size;

    public Model model;
    public ModelInstance modelInstance;

    @Override
    public void dispose() {
        model.dispose();
    }
}
