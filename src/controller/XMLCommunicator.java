package controller;

import model.backpack.Backpack;
import model.backpack.BackpackManager;
import model.shapes.Shape;
import model.shapes.Cube;
import model.shapes.Cylinder;
import model.shapes.Globe;
import model.shapes.Parallelepiped;
import model.shapes.Tetrahedron;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.IOException;

public class XMLCommunicator {
    private static final String BACKPACK_SCHEMA = "BackpackSchema.xsd";
    private Type type_;
    enum Type {
        CUBE,
        CYLINDER,
        GLOBE,
        PARALLELEPIPED,
        TETRAHEDRON
    }


    public static class XMLException extends RuntimeException {
        public XMLException(String message) {
            super(message);
        }
    }

    public void saveShapesToXML(String path, Backpack<Shape> backpack) throws ParserConfigurationException, TransformerException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.newDocument();

        Element root = document.createElement("backpack");
        root.appendChild(createElement(document, "capacity", backpack.capacity().toString()));
        Element shapes = document.createElement("shapes");

        for (Shape shape : backpack.getItems()) {
            Element shapeNode = document.createElement("shape");
            shapeNode.setAttribute("type", shape.type());
//            shapeNode.setAttribute("isPretty", shape.isPretty() ? "Yes" : "No");
            shapeNode.setAttribute("side", shape.getSide().toString());
            type_ = Type.valueOf(shape.type().toUpperCase());
            switch (type_){
                case CUBE:
                case GLOBE:
                case TETRAHEDRON:
                    break;
                case CYLINDER:
                    shapeNode.setAttribute("high", ((Cylinder) shape).getHigh().toString());
                    break;
                case PARALLELEPIPED:
                    shapeNode.setAttribute("high", ((Parallelepiped) shape).getHigh().toString());
                    shapeNode.setAttribute("length", ((Parallelepiped) shape).getLength().toString());
                    break;
            }
            shapes.appendChild(shapeNode);
        }

        root.appendChild(shapes);
        document.appendChild(root);

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

        DOMSource source = new DOMSource(document);
        File file = new File(path);
        StreamResult streamFile = new StreamResult(file);
        transformer.transform(source, streamFile);
    }


    private boolean correctXML(File file, File schema) {
        try {
            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema scheme = factory.newSchema(schema);
            Validator validator = scheme.newValidator();
            validator.validate(new StreamSource(file));
        } catch (SAXException | IOException e) {
            return false;
        }
        return true;
    }

    public Backpack<Shape> loadShapesFromXML(String path) throws ParserConfigurationException, TransformerException, IOException, SAXException {
        File file = new File(path);
        File schemaFile = new File(BACKPACK_SCHEMA);

        if (!correctXML(file, schemaFile)) {
            throw new XMLException("Some problems with XML file");
        }


        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(file);
        document.getDocumentElement().normalize();

        Node backpackSizeNode = document.getDocumentElement().getElementsByTagName("capacity").item(0);
        Double backpackSize = Double.parseDouble(backpackSizeNode.getTextContent());
        Backpack<Shape> backpack = new Backpack<>(backpackSize);

        NodeList shapes = document.getElementsByTagName("shape");

        for(int i = 0; i < shapes.getLength(); i++){
            Node shape = shapes.item(i);
            NamedNodeMap attributes = shape.getAttributes();
            String type = attributes.getNamedItem("type").getNodeValue();
            Double side = Double.parseDouble(attributes.getNamedItem("side").getNodeValue());

            type_ = Type.valueOf(type.toUpperCase());
            Shape currentShape;
            switch (type_){
                case CUBE:
                    currentShape = new Cube(side);
                    break;
                case GLOBE:
                    currentShape = new Globe(side);
                    break;
                case TETRAHEDRON:
                    currentShape = new Tetrahedron(side);
                    break;
                case CYLINDER:
                    Double high = Double.parseDouble(attributes.getNamedItem("high").getNodeValue());
                    currentShape = new Cylinder(side, high);
//                    shapeNode.setAttribute("high", ((Cylinder) shape).getHigh().toString());
                    break;
                case PARALLELEPIPED:
                    Double highPar = Double.parseDouble(attributes.getNamedItem("high").getNodeValue());
                    Double length = Double.parseDouble(attributes.getNamedItem("length").getNodeValue());
                    currentShape = new Parallelepiped(length, side, highPar);
//                    shapeNode.setAttribute("high", ((Parallelepiped) shape).getHigh().toString());
//                    shapeNode.setAttribute("length", ((Parallelepiped) shape).getLength().toString());
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + type);
            }
            backpack.put(currentShape);
        }
        return backpack;
    }

    private Node createElement(Document document, String name,
                               String value) {
        Element node = document.createElement(name);
        node.appendChild(document.createTextNode(value));
        return node;
    }
}
