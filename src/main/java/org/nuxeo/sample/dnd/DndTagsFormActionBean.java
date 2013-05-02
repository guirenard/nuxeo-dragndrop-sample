/*
 * (C) Copyright 2006-2013 Nuxeo SAS (http://nuxeo.com/) and contributors.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser General Public License
 * (LGPL) version 2.1 which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl.html
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * Contributors:
 *     ldoguin
 *
 */
package org.nuxeo.sample.dnd;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Factory;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Observer;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.intercept.BypassInterceptors;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.webapp.helpers.EventNames;

/**
 * Very basic implementation of a tag widget backing bean for DND purpose.
 *.
 * @author ldoguin (ldoguin@nuxeo.com)
 */
@Name("dndTagsFormActions")
@Scope(ScopeType.CONVERSATION)
public class DndTagsFormActionBean implements Serializable {

    private static final long serialVersionUID = 1L;

    List<String> dndTags;

    private String tagLabel;
    
    @Factory(value = "dndTags", scope = ScopeType.CONVERSATION)
    public List<String> getDndTags() throws ClientException {
        if (dndTags == null) {
            dndTags = new ArrayList<String>();
        }
        return dndTags;
    }

    public String addDndTag() throws ClientException {
        tagLabel = cleanLabel(tagLabel);
        if (!StringUtils.isBlank(tagLabel)) {
            dndTags.add(tagLabel);
        }
        reset();
        return null;
    }

    public String removeDndTag(String label) {
        dndTags.remove(label);
        reset();
        return null;
    }

    public void reset() {
        tagLabel = null;
    }

    public String getTagLabel() {
        return tagLabel;
    }

    public void setTagLabel(String tagLabel) {
        this.tagLabel = tagLabel;
    }

    protected static String cleanLabel(String label) {
        label = label.toLowerCase(); // lowercase
        label = label.replace(" ", ""); // no spaces
        label = label.replace("\\", ""); // dubious char
        label = label.replace("'", ""); // dubious char
        label = label.replace("%", ""); // dubious char
        return label;
    }

    @Observer(value = { EventNames.DOCUMENT_SELECTION_CHANGED }, create = false)
    @BypassInterceptors
    public void documentChanged() {
        dndTags = null;
    }

}
