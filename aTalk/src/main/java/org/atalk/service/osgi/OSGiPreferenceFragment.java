/*
 * Jitsi, the OpenSource Java VoIP and Instant Messaging client.
 *
 * Distributable under LGPL license. See terms of license at gnu.org.
 */
package org.atalk.service.osgi;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.preference.PreferenceFragmentCompat;

import org.osgi.framework.BundleContext;

/**
 * Class can be used to build {@link androidx.preference.PreferenceFragmentCompat}s that require OSGI services access.
 *
 * @author Pawel Domas
 */
public class OSGiPreferenceFragment extends PreferenceFragmentCompat implements OSGiUiPart
{
    protected BundleContext osgiContext;
    private boolean viewCreated = false;
    private boolean osgiNotified = false;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey)
    {
        viewCreated = true;
        if (!osgiNotified && osgiContext != null) {
            onOSGiConnected();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onDestroyView()
    {
        viewCreated = false;
        osgiNotified = false;
        super.onDestroyView();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onAttach(@NonNull Context context)
    {
        super.onAttach(context);
        OSGiActivity osGiActivity = (OSGiActivity) getActivity();
        osGiActivity.registerOSGiFragment(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onDetach()
    {
        ((OSGiActivity) getActivity()).unregisterOSGiFragment(this);
        super.onDetach();
    }

    /**
     * Fired when OSGI is started and the <code>bundleContext</code> is available.
     *
     * @param bundleContext the OSGI bundle context.
     */
    public void start(BundleContext bundleContext)
            throws Exception
    {
        this.osgiContext = bundleContext;
        if (viewCreated && !osgiNotified) {
            onOSGiConnected();
        }
    }

    /**
     * Method fired when OSGI context is attached, but after the <code>View</code> is created.
     */
    protected void onOSGiConnected()
    {
        osgiNotified = true;
    }

    /**
     * Fired when parent <code>OSGiActivity</code> is being stopped or this fragment is being detached.
     *
     * @param bundleContext the OSGI bundle context.
     */
    public void stop(BundleContext bundleContext)
            throws Exception
    {
        this.osgiContext = null;
    }
}
