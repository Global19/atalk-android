/*
 * Jitsi, the OpenSource Java VoIP and Instant Messaging client.
 *
 * Distributable under LGPL license. See terms of license at gnu.org.
 */
package org.atalk.android.gui.util;

import android.content.Context;

import net.java.sip.communicator.service.gui.AlertUIService;

import org.atalk.android.gui.dialogs.DialogActivity;

/**
 * The <code>AlertUIServiceImpl</code> provides an android implementation of the <code>AlertUIService</code>.
 *
 * @author Yana Stamcheva
 * @author Eng Chong Meng
 */
public class AlertUIServiceImpl implements AlertUIService
{
    private final Context androidContext;

    /**
     * Creates an instance of <code>AlertUIServiceImpl</code>.
     *
     * @param androidContext the android application context.
     */
    public AlertUIServiceImpl(Context androidContext)
    {
        this.androidContext = androidContext;
    }

    /**
     * Shows an alert dialog with the given title message and exception corresponding to the error.
     *
     * @param title the title of the dialog
     * @param message the message to be displayed
     * @param e the exception corresponding to the error
     */
    public void showAlertDialog(String title, String message, Throwable e)
    {
        DialogActivity.showDialog(androidContext, title, message);
        System.err.println("Exception occurred: %s" + e);
    }

    /**
     * Shows an alert dialog with the given title, message and type of message.
     *
     * @param title the title of the error dialog
     * @param message the message to be displayed
     * @param type the dialog type (warning or error)
     */
    public void showAlertDialog(String title, String message, int type)
    {
        // TODO: Implement type warning, error, etc.
        DialogActivity.showDialog(androidContext, title, message);
    }

    /**
     * Shows an notification pop-up which can be clicked. An error dialog is shown when the notification is clicked.
     *
     * @param title the title of the error dialog and the notification pop-up
     * @param message the message to be displayed in the error dialog and the pop-up
     */
    public void showAlertPopup(String title, String message)
    {
        DialogActivity.showDialog(androidContext, title, message);
    }

    /**
     * Shows an notification pop-up which can be clicked. An error dialog is shown when the notification is clicked.
     *
     * @param title the title of the error dialog and the notification pop-up
     * @param message the message to be displayed in the error dialog and the pop-up
     * @param e the exception that can be shown in the error dialog
     */
    public void showAlertPopup(String title, String message, Throwable e)
    {
        DialogActivity.showDialog(androidContext, title, message);
    }

    /**
     * Shows an notification pop-up which can be clicked. An error dialog is shown when the notification is clicked.
     *
     * @param title the title of the notification pop-up
     * @param message the message of the pop-up
     * @param errorDialogTitle the title of the error dialog
     * @param errorDialogMessage the message of the error dialog
     */
    public void showAlertPopup(String title, String message, String errorDialogTitle, String errorDialogMessage)
    {
        DialogActivity.showDialog(androidContext, title, message);
    }

    /**
     * Shows an notification pop-up which can be clicked. An error dialog is shown when the notification is clicked.
     *
     * @param title the title of the notification pop-up
     * @param message the message of the pop-up
     * @param errorDialogTitle the title of the error dialog
     * @param errorDialogMessage the message of the error dialog
     * @param e the exception that can be shown in the error dialog
     */
    public void showAlertPopup(String title, String message, String errorDialogTitle, String errorDialogMessage, Throwable e)
    {
        DialogActivity.showDialog(androidContext, title, message);
    }

    /**
     * Releases the resources acquired by this instance throughout its lifetime and removes the listeners.
     */
    public void dispose()
    {
    }
}