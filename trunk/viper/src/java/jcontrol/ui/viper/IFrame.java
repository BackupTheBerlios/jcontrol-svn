/*
 * $Id$
 * 
 * Copyright (C) 2005-2009 The JControl Group and individual authors listed below
 * 
 * This library is free software; you can redistribute it and/or modify it under the terms of the
 * GNU Lesser General Public License as published by the Free Software Foundation; either version
 * 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License along with this library;
 * if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA
 * 02111-1307 USA
 */
package jcontrol.ui.viper;

/**
 * An application must have (or self be) a <code>Frame</code> instance. A frame represents the very
 * top level element of the application's component tree. Here, all drawing action is managed. The
 * frame is also responsible for the control of user input such as touch or keyboard actions. The
 * frame consists of two main parts: The outline and the content.
 * <ul>
 * <li>The outline is a <code>Component</code> (may even a <code>Container</code>) that represents
 * the static part of an application. This can be a menu, a logo, a background image, an analog
 * clock somewhere in a corner or whatever. The outline is optional and should normally not be
 * replaced at run-time.</li>
 * <li>The content is a <code>Container</code> that represents the dynamic part of an application.
 * Adding components to the frame means to add components to the frame's content. The content can
 * even be replaced completely by the application using the <code>setContent</code> method.</li>
 * </ul>
 * 
 * @author Marcus Timmermann
 * @since Viper 1.0
 * @version $Revision$
 */
public interface IFrame extends IContainer {

    /**
     * Clears the focus tree under the specified container. This method is for internal use and
     * should not be called by the application.<br>
     * 
     * @param container a container
     */
    public void clearFocus(IContainer container);

    /**
     * Returns the current content or <code>null</code> if there is none.
     * 
     * @return the current content or <code>null</code> if there is none.
     */
    public Container getContent();

    /**
     * Returns the current outline or <code>null</code> if there is none.
     * 
     * @return the current outline or <code>null</code> if there is none.
     */
    public Component getOutline();

    /**
     * Tries to transfer the keyboard focus to the given component. This method is for internal use
     * and should not be called by the application. This method may be empty in some frame
     * implementations. <br>
     * 
     * @param component the component to transfer the focus to
     * @return true if this method was successful
     * @see IFocusable#requestFocus()
     */
    public boolean requestFocus(Component component);

    /**
     * <p>
     * Sets/replaces the frame's content. Setting this value to <code>null</code> will destroy the
     * current content. All handles within the current content will be reset to pave the way for the
     * garbage collector. In this case, the frame's content will be <code>null</code> until this
     * method is called again with a new container instance as argument or until the
     * <code>add(Component)</code> method is called. In this case a new default content will be
     * created internally.<br>
     * As the content is the application's root container, this method can easily be used to
     * implement a multi-page application by creating a number of containers, one for each page.
     * Calling this method with any container instance will erase the whole display content and
     * replace it with the new one. A good idea to reduce memory consumption is to set the old
     * content to <code>null</code> before creating a new one. Thus, the garbage collector can
     * release all memory resources that have been used by the old content <i>before</i> the new
     * container is created that maybe needs this memory.
     * </p>
     * Example:<br>
     * 
     * <pre>
     * public class FirstPage extends Container {
     *     public FirstPage() {
     *         super(2);
     *         // create some components here
     *         add(new Button(&quot;OK&quot;, ...);
     *         add(new Button(&quot;Cancel&quot;, ...);
     *     }
     * }
     * 
     * public class SecondPage extends Container {
     *     public SecondPage() {
     *         super(2);
     *         // create some other components here
     *         add(new Button(&quot;Abort&quot;, ...);
     *         add(new Button(&quot;Retry&quot;, ...);
     *     }
     * }
     * 
     * // important part of the main Application
     * public class PageDemo extends Frame {
     * 
     *     public PageDemo() {
     *         // set first page as content 
     *         setContent(new FirstPage());
     *         // make the frame visible
     *         setVisible(true);
     *         ...
     *         // some code
     *         ...
     *         // delete current content
     *         setContent(null);
     *         // now, it's time for the garbage collector to work
     * 
     *         // switch to second page
     *         setContent(new SecondPage());
     *     }
     * }
     * </pre>
     * 
     * @param container a container instance or <code>null</code>.
     */
    public void setContent(Container container);

    /**
     * Sets a static element on the frame. The static element of a frame can be e.g. a clock or a
     * logo or whatever. It will not disappear when the content changes.
     * 
     * @param component a component to use as static element on the frame.
     */
    public void setOutline(Component component);

    /**
     * Transfers the focus in the given container to the next <code>IFocusable</code> element. This
     * method is for internal use and should not be called by the application. This method may be
     * empty in some frame implementations.<br>
     * 
     * @param c the container
     * @param direction IFocusable.TRANSFER_FOCUS_FORWARD or IFocusable.TRANSFER__FOCUS_BACKWARD
     */
    public boolean transferFocus(IContainer c, int direction);
}
