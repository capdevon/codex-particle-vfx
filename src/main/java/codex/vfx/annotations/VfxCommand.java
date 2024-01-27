package codex.vfx.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Marks a method that can be called when a corresponding event occurs.
 * 
 * @author codex
 */
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface VfxCommand {

    /**
     * 
     * @return
     */
    String name();

}
