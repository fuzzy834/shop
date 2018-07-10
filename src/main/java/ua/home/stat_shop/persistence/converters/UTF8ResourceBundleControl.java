package ua.home.stat_shop.persistence.converters;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

public class UTF8ResourceBundleControl extends ResourceBundle.Control {

    @Override
    public ResourceBundle newBundle(String baseName, Locale locale, String format, ClassLoader loader, boolean reload)
            throws IOException {
        String bundleName = toBundleName(baseName, locale);
        String resourceName = toResourceName(bundleName, "properties");
        try(InputStream stream = loader.getResourceAsStream(resourceName);
            Reader reader = new InputStreamReader(stream, "UTF-8")) {
            return new PropertyResourceBundle(reader);
        }
    }
}
