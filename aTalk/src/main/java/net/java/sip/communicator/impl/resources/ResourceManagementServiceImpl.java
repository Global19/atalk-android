/*
 * Jitsi, the OpenSource Java VoIP and Instant Messaging client.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.java.sip.communicator.impl.resources;

import net.java.sip.communicator.impl.resources.util.SkinJarBuilder;
import net.java.sip.communicator.service.gui.UIService;
import net.java.sip.communicator.service.resources.*;
import net.java.sip.communicator.util.ServiceUtils;

import javax.swing.ImageIcon;
import org.osgi.framework.ServiceEvent;

import java.io.*;
import java.net.URL;

import timber.log.Timber;

/**
 * A default implementation of the <code>ResourceManagementService</code>.
 *
 * @author Damian Minkov
 * @author Yana Stamcheva
 * @author Lubomir Marinov
 * @author Adam Netocny
 * @author Eng Chong Meng
 */
public class ResourceManagementServiceImpl extends AbstractResourcesService
{
    /**
     * UI Service reference.
     */
    private UIService uiService = null;

    /**
     * Initializes already registered default resource packs.
     */
    ResourceManagementServiceImpl()
    {
        super(ResourceManagementActivator.bundleContext);
        UIService serv = getUIService();
        if (serv != null) {
            serv.repaintUI();
        }
    }

    /**
     * Returns the <code>UIService</code> obtained from the bundle context.
     *
     * @return the <code>UIService</code> obtained from the bundle context
     */
    private UIService getUIService()
    {
        if (uiService == null) {
            uiService = ServiceUtils.getService(ResourceManagementActivator.bundleContext, UIService.class);
        }
        return uiService;
    }

    /**
     * Gets a reference to the <code>UIService</code> when this one is registered.
     *
     * @param event the <code>ServiceEvent</code> that has notified us
     */
    @Override
    public void serviceChanged(ServiceEvent event)
    {
        super.serviceChanged(event);

        Object sService = ResourceManagementActivator.bundleContext.getService(event.getServiceReference());

        if (sService instanceof UIService && uiService == null
                && event.getType() == ServiceEvent.REGISTERED) {
            uiService = (UIService) sService;
            uiService.repaintUI();
        }
        else if (sService instanceof UIService
                && event.getType() == ServiceEvent.UNREGISTERING) {
            if (uiService != null && uiService.equals(sService)) {
                uiService = null;
            }
        }
    }

    /**
     * Repaints the whole UI when a skin pack has changed.
     */
    @Override
    protected void onSkinPackChanged()
    {
        UIService serv = getUIService();
        if (serv != null) {
            serv.repaintUI();
        }
    }

    /**
     * Returns the int representation of the color corresponding to the given key.
     *
     * @param key The key of the color in the colors properties file.
     * @return the int representation of the color corresponding to the given key.
     */
    public int getColor(String key)
    {
        String res = getColorResources().get(key);
        if (res == null) {
            Timber.e("Missing color resource for key: %s", key);
            return 0xFFFFFF;
        }
        else
            return Integer.parseInt(res, 16);
    }

    /**
     * Returns the string representation of the color corresponding to the
     * given key.
     *
     * @param key The key of the color in the colors properties file.
     * @return the string representation of the color corresponding to the
     * given key.
     */
    public String getColorString(String key)
    {
        String res = getColorResources().get(key);
        if (res == null) {
            Timber.e("Missing color resource for key: %s", key);
            return "0xFFFFFF";
        }
        else
            return res;
    }

    /**
     * Returns the <code>InputStream</code> of the image corresponding to the given path.
     *
     * @param path The path to the image file.
     * @return the <code>InputStream</code> of the image corresponding to the given path.
     */
    public InputStream getImageInputStreamForPath(String path)
    {
        SkinPack skinPack = getSkinPack();
        if (skinPack != null) {
            if (skinPack.getClass().getClassLoader().getResourceAsStream(path) != null) {
                return skinPack.getClass().getClassLoader().getResourceAsStream(path);
            }
        }

        ImagePack imagePack = getImagePack();
        if (path != null && imagePack != null)
            return imagePack.getClass().getClassLoader().getResourceAsStream(path);
        return null;
    }

    /**
     * Returns the <code>InputStream</code> of the image corresponding to the given key.
     *
     * @param streamKey The identifier of the image in the resource properties file.
     * @return the <code>InputStream</code> of the image corresponding to the given key.
     */
    public InputStream getImageInputStream(String streamKey)
    {
        String path = getImagePath(streamKey);

        if (path == null || path.length() == 0) {
            Timber.w("Missing resource for key: %s", streamKey);
            return null;
        }
        return getImageInputStreamForPath(path);
    }

    /**
     * Returns the <code>URL</code> of the image corresponding to the given key.
     *
     * @param urlKey The identifier of the image in the resource properties file.
     * @return the <code>URL</code> of the image corresponding to the given key
     */
    public URL getImageURL(String urlKey)
    {
        String path = getImagePath(urlKey);
        if (path == null || path.length() == 0) {
            Timber.i("Missing resource for key: %s", urlKey);
            return null;
        }
        return getImageURLForPath(path);
    }

    /**
     * Returns the <code>URL</code> of the image corresponding to the given path.
     *
     * @param path The path to the given image file.
     * @return the <code>URL</code> of the image corresponding to the given path.
     */
    public URL getImageURLForPath(String path)
    {
        SkinPack skinPack = getSkinPack();
        if (skinPack != null) {
            if (skinPack.getClass().getClassLoader().getResource(path) != null) {
                return skinPack.getClass().getClassLoader().getResource(path);
            }
        }
        ImagePack imagePack = getImagePack();
        return imagePack.getClass().getClassLoader().getResource(path);
    }

    /**
     * Returns the <code>URL</code> of the sound corresponding to the given property key.
     *
     * @return the <code>URL</code> of the sound corresponding to the given property key.
     */
    public URL getSoundURL(String urlKey)
    {
        String path = getSoundPath(urlKey);

        if (path == null || path.length() == 0) {
            Timber.w("Missing resource for key: %s", urlKey);
            return null;
        }
        return getSoundURLForPath(path);
    }

    /**
     * Returns the <code>URL</code> of the sound corresponding to the given path.
     *
     * @param path the path, for which we're looking for a sound URL
     * @return the <code>URL</code> of the sound corresponding to the given path.
     */
    public URL getSoundURLForPath(String path)
    {
        return getSoundPack().getClass().getClassLoader().getResource(path);
    }

    /**
     * Loads an image from a given image identifier.
     *
     * @param imageID The identifier of the image.
     * @return The image for the given identifier.
     */
    @Override
    public byte[] getImageInBytes(String imageID)
    {
        InputStream in = getImageInputStream(imageID);
        if (in == null)
            return null;

        byte[] image = null;
        try {
            image = new byte[in.available()];
            in.read(image);
        } catch (IOException e) {
            Timber.e(e, "Failed to load image:%s", imageID);
        }
        return image;
    }

    /**
     * Loads an image from a given image identifier.
     *
     * @param imageID The identifier of the image.
     * @return The image for the given identifier.
     */
    @Override
    public ImageIcon getImage(String imageID)
    {
        URL imageURL = getImageURL(imageID);
        return (imageURL == null) ? null : new ImageIcon(imageURL);
    }

    /**
     * Builds a new skin bundle from the zip file content.
     *
     * @param zipFile Zip file with skin information.
     * @return <code>File</code> for the bundle.
     * @throws Exception When something goes wrong.
     */
    public File prepareSkinBundleFromZip(File zipFile)
            throws Exception
    {
        return SkinJarBuilder.createBundleFromZip(zipFile, getImagePack());
    }

    /**
     * Gets the specified setting from the config service if present, otherwise
     * from the embedded resources (resources/config/defaults.properties).
     *
     * @param key The setting to lookup.
     * @return The setting for the key or {@code null} if not found.
     */
    @Override
    public String getSettingsString(String key)
    {
        Object configValue = ResourceManagementActivator.getConfigService().getProperty(key);
        if (configValue == null) {
            configValue = super.getSettingsString(key);
        }
        return configValue == null ? null : configValue.toString();
    }
}
