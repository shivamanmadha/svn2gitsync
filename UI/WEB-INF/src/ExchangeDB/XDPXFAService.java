package ExchangeDB;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.w3c.dom.Document;

import flex.acrobat.pdf.XDPXFAHelper;
import flex.messaging.FlexContext;
import flex.messaging.FlexSession;
import flex.messaging.util.UUIDUtils;

public class XDPXFAService
{
    public XDPXFAService()
    {
    }

    public Object generatePDF(Document dataset) throws IOException
    {
        // Open shell XDP containing XFA template
        String source = FlexContext.getServletContext().getRealPath("/pdfgen/company.xdp");
        XDPXFAHelper helper = new XDPXFAHelper();
        helper.open(source);

        // Import XFA dataset
        helper.importDataset(dataset);

        // Save new PDF as a byte array in the current session
        byte[] bytes = helper.saveToByteArray();
        String uuid = UUIDUtils.createUUID(false);
        FlexSession session = FlexContext.getFlexSession();
        session.setAttribute(uuid, bytes);

        // Close any resources
        helper.close();

        HttpServletRequest req = FlexContext.getHttpRequest();
        String contextRoot = "/samples";
        if (req != null)
            contextRoot = req.getContextPath();

        return contextRoot + "/dynamic-pdf?id=" + uuid + "&;jsessionid=" + session.getId();
    }
}
