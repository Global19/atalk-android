/*
 * Jitsi, the OpenSource Java VoIP and Instant Messaging client.
 *
 * Distributable under LGPL license. See terms of license at gnu.org.
 */
package org.atalk.android.gui.actionbar;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import net.java.sip.communicator.service.globaldisplaydetails.GlobalDisplayDetailsService;
import net.java.sip.communicator.service.globaldisplaydetails.event.*;
import net.java.sip.communicator.service.protocol.*;
import net.java.sip.communicator.service.protocol.globalstatus.GlobalStatusEnum;
import net.java.sip.communicator.service.protocol.globalstatus.GlobalStatusService;
import net.java.sip.communicator.util.StatusUtil;
import net.java.sip.communicator.util.account.AccountUtils;

import org.apache.commons.lang3.StringUtils;
import org.atalk.android.R;
import org.atalk.android.gui.AndroidGUIActivator;
import org.atalk.android.gui.aTalk;
import org.atalk.android.gui.account.AndroidLoginRenderer;
import org.atalk.android.gui.menu.GlobalStatusMenu;
import org.atalk.android.gui.util.event.EventListener;
import org.atalk.android.gui.widgets.ActionMenuItem;
import org.atalk.service.osgi.OSGiFragment;

import java.util.Collection;

/**
 * Fragment when added to Activity will display global display details like avatar, display name
 * and status. External events will also trigger a change to the contents.
 * When status is clicked a popup menu is displayed allowing user to set global presence status.
 *
 * @author Pawel Domas
 * @author Eng Chong Meng
 */
public class ActionBarStatusFragment extends OSGiFragment
        implements EventListener<PresenceStatus>, GlobalDisplayDetailsListener
{
    /**
     * The online status.
     */
    private static final int ONLINE = 1;

    /**
     * The offline status.
     */
    private static final int OFFLINE = 2;

    /**
     * The free for chat status.
     */
    private static final int FFC = 3;

    /**
     * The away status.
     */
    private static final int AWAY = 4;

    /**
     * The away status.
     */
    private static final int EXTENDED_AWAY = 5;

    /**
     * The do not disturb status.
     */
    private static final int DND = 6;

    private static int ACTION_ID = DND + 1;

    /**
     * The global status menu.
     */
    private GlobalStatusMenu globalStatusMenu;
    private AppCompatActivity mContext;

    private static GlobalDisplayDetailsService displayDetailsService;
    private static AndroidLoginRenderer loginRenderer;

    /**
     * {@inheritDoc}
     */
    @Override
    public void onAttach(@NonNull Context context)
    {
        super.onAttach(context);
        mContext = (AppCompatActivity) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        loginRenderer = AndroidGUIActivator.getLoginRenderer();
        displayDetailsService = AndroidGUIActivator.getGlobalDisplayDetailsService();
        globalStatusMenu = createGlobalStatusMenu();

        View actionBarView = mContext.findViewById(R.id.actionBarView);
        actionBarView.setOnClickListener(v -> {
            globalStatusMenu.show(actionBarView);
            globalStatusMenu.setAnimStyle(GlobalStatusMenu.ANIM_REFLECT);
        });
    }

    @Override
    public void onResume()
    {
        super.onResume();
        loginRenderer.addGlobalStatusListener(this);
        onChangeEvent(loginRenderer.getGlobalStatus());

        displayDetailsService.addGlobalDisplayDetailsListener(this);
        setGlobalAvatar(displayDetailsService.getDisplayAvatar(null));
        setGlobalDisplayName(displayDetailsService.getDisplayName(null));
    }

    @Override
    public void onPause()
    {
        super.onPause();
        loginRenderer.removeGlobalStatusListener(this);
        displayDetailsService.removeGlobalDisplayDetailsListener(this);
    }

    /**
     * Creates the <code>GlobalStatusMenu</code>.
     *
     * @return the newly created <code>GlobalStatusMenu</code>
     */
    private GlobalStatusMenu createGlobalStatusMenu()
    {
        Resources res = getResources();
        ActionMenuItem ffcItem = new ActionMenuItem(FFC,
                res.getString(R.string.service_gui_FFC_STATUS),
                ResourcesCompat.getDrawable(res, R.drawable.global_ffc, null));
        ActionMenuItem onlineItem = new ActionMenuItem(ONLINE,
                res.getString(R.string.service_gui_ONLINE),
                ResourcesCompat.getDrawable(res, R.drawable.global_online, null));
        ActionMenuItem offlineItem = new ActionMenuItem(OFFLINE,
                res.getString(R.string.service_gui_OFFLINE),
                ResourcesCompat.getDrawable(res, R.drawable.global_offline, null));
        ActionMenuItem awayItem = new ActionMenuItem(AWAY,
                res.getString(R.string.service_gui_AWAY_STATUS),
                ResourcesCompat.getDrawable(res, R.drawable.global_away, null));
        ActionMenuItem extendedAwayItem = new ActionMenuItem(EXTENDED_AWAY,
                res.getString(R.string.service_gui_EXTENDED_AWAY_STATUS),
                ResourcesCompat.getDrawable(res, R.drawable.global_extended_away, null));
        ActionMenuItem dndItem = new ActionMenuItem(DND,
                res.getString(R.string.service_gui_DND_STATUS),
                ResourcesCompat.getDrawable(res, R.drawable.global_dnd, null));

        final GlobalStatusMenu globalStatusMenu = new GlobalStatusMenu(mContext);
        globalStatusMenu.addActionItem(ffcItem);
        globalStatusMenu.addActionItem(onlineItem);
        globalStatusMenu.addActionItem(offlineItem);
        globalStatusMenu.addActionItem(awayItem);
        globalStatusMenu.addActionItem(extendedAwayItem);
        globalStatusMenu.addActionItem(dndItem);

        // Add all registered PPS users to the presence status menu
        Collection<ProtocolProviderService> registeredProviders = AccountUtils.getRegisteredProviders();
        for (ProtocolProviderService pps : registeredProviders) {
            AccountID accountId = pps.getAccountID();
            String userJid = accountId.getAccountJid();
            Drawable icon = ResourcesCompat.getDrawable(res, R.drawable.jabber_status_online, null);

            ActionMenuItem actionItem = new ActionMenuItem(ACTION_ID++, userJid, icon);
            globalStatusMenu.addActionItem(actionItem, pps);
        }

        globalStatusMenu.setOnActionItemClickListener((source, pos, actionId) -> {
            if (actionId <= DND)
                publishGlobalStatus(actionId);
        });

        globalStatusMenu.setOnDismissListener(() -> {
            // TODO: Add a dismiss action.
        });
        return globalStatusMenu;
    }

    /**
     * Publishes global status on separate thread to prevent <code>NetworkOnMainThreadException</code>.
     *
     * @param newStatus new global status to set.
     */
    private void publishGlobalStatus(final int newStatus)
    {
        /*
         * Runs publish status on separate thread to prevent NetworkOnMainThreadException
         */
        new Thread(() -> {
            GlobalStatusService globalStatusService = AndroidGUIActivator.getGlobalStatusService();
            switch (newStatus) {
                case FFC:
                    globalStatusService.publishStatus(GlobalStatusEnum.FREE_FOR_CHAT);
                    break;
                case ONLINE:
                    globalStatusService.publishStatus(GlobalStatusEnum.ONLINE);
                    break;
                case OFFLINE:
                    globalStatusService.publishStatus(GlobalStatusEnum.OFFLINE);
                    break;
                case AWAY:
                    globalStatusService.publishStatus(GlobalStatusEnum.AWAY);
                    break;
                case EXTENDED_AWAY:
                    globalStatusService.publishStatus(GlobalStatusEnum.EXTENDED_AWAY);
                    break;
                case DND:
                    globalStatusService.publishStatus(GlobalStatusEnum.DO_NOT_DISTURB);
                    break;
            }
        }).start();
    }

    @Override
    public void onChangeEvent(final PresenceStatus presenceStatus)
    {
        if ((presenceStatus == null) || (mContext == null))
            return;

        runOnUiThread(() -> {
            String mStatus = presenceStatus.getStatusName();
            ActionBarUtil.setSubtitle(mContext, mStatus);
            ActionBarUtil.setStatus(mContext, StatusUtil.getStatusIcon(presenceStatus));

            MenuItem mOnOffLine = ((aTalk) mContext).getMenuItemOnOffLine();
            // Proceed only if mOnOffLine has been initialized
            if (mOnOffLine != null) {
                boolean isOffline = GlobalStatusEnum.OFFLINE_STATUS.equals(mStatus);
                int itemId = isOffline
                        ? R.string.service_gui_SIGN_IN
                        : R.string.service_gui_SIGN_OUT;
                mOnOffLine.setTitle(itemId);
            }
        });
    }

    /**
     * Indicates that the global avatar has been changed.
     */
    @Override
    public void globalDisplayAvatarChanged(final GlobalAvatarChangeEvent evt)
    {
        runOnUiThread(() -> setGlobalAvatar(evt.getNewAvatar()));
    }

    /**
     * Indicates that the global display name has been changed.
     */
    @Override
    public void globalDisplayNameChanged(final GlobalDisplayNameChangeEvent evt)
    {
        runOnUiThread(() -> setGlobalDisplayName(evt.getNewDisplayName()));
    }

    /**
     * Sets the global avatar in the action bar.
     *
     * @param avatar the byte array representing the avatar to set
     */
    private void setGlobalAvatar(final byte[] avatar)
    {
        if (avatar != null && avatar.length > 0) {
            ActionBarUtil.setAvatar(mContext, avatar);
        }
        else {
            ActionBarUtil.setAvatar(mContext, R.drawable.ic_icon);
        }
    }

    /**
     * Sets the global display name in the action bar as 'Me' if multiple accounts are involved, otherwise UserJid.
     *
     * @param name the display name to set
     */
    private void setGlobalDisplayName(final String name)
    {
        String displayName = name;
        Collection<ProtocolProviderService> pProviders = AccountUtils.getRegisteredProviders();

        if (StringUtils.isEmpty(displayName) && (pProviders.size() == 1)) {
            displayName = pProviders.iterator().next().getAccountID().getUserID();
        }
        if (pProviders.size() > 1)
            displayName = getString(R.string.service_gui_ACCOUNT_ME);
        ActionBarUtil.setTitle(mContext, displayName);
    }
}
