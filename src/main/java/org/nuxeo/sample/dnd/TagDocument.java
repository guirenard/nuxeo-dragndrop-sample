/*
 * (C) Copyright 2013 Nuxeo SA (http://nuxeo.com/) and contributors.
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
 */

package org.nuxeo.sample.dnd;

import java.io.Serializable;
import java.util.List;

import org.jboss.seam.contexts.Contexts;
import org.nuxeo.ecm.automation.OperationContext;
import org.nuxeo.ecm.automation.core.Constants;
import org.nuxeo.ecm.automation.core.annotations.Context;
import org.nuxeo.ecm.automation.core.annotations.Operation;
import org.nuxeo.ecm.automation.core.annotations.OperationMethod;
import org.nuxeo.ecm.automation.core.collectors.DocumentModelCollector;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.platform.tag.TagService;

/**
 * @author ldoguin
 */
@Operation(id = TagDocument.ID, category = Constants.CAT_DOCUMENT, requires = Constants.SEAM_CONTEXT, label = "TagDocument", description = "")
public class TagDocument {

    public static final String ID = "Document.TagDocument";

    @Context
    protected CoreSession session;

    @Context
    protected OperationContext ctx;

    @Context
    TagService tagService;

    @OperationMethod(collector = DocumentModelCollector.class)
    public DocumentModel run(DocumentModel doc) throws Exception {
        if (!isSeamContextAvailable()) {
            return doc;
        }
        List<String> tags = getDndTagsFormAction().getDndTags();
        for (Serializable tag : tags) {
            tagService.tag(session, doc.getId(), (String) tag,
                    ctx.getPrincipal().getName());
        }
        return doc;
    }

    public static boolean isSeamContextAvailable() {
        return Contexts.isSessionContextActive();
    }

    public static DndTagsFormActionBean getDndTagsFormAction() {
        return (DndTagsFormActionBean) Contexts.getConversationContext().get(
                "dndTagsFormActions");
    }

}
