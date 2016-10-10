package nursulaeman.catetduit.validation;

/**
 * Created by nur on 10/10/16.
 */
public class Validation {

    public boolean isValidText (String text) {
        if (!text.equals("")) {
            return true;
        }
        return false;
    }

}
