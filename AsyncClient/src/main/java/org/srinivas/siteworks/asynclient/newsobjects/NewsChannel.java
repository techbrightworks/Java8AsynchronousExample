
package org.srinivas.siteworks.asynclient.newsobjects;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{}title"/>
 *         &lt;element ref="{}description"/>
 *         &lt;element ref="{}link"/>
 *         &lt;element ref="{}newsItems"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "title",
    "description",
    "link",
    "newsItems"
})
@XmlRootElement(name = "newsChannel")
public class NewsChannel {

    @XmlElement(required = true)
    protected String title;
    @XmlElement(required = true)
    protected String description;
    @XmlElement(required = true)
    @XmlSchemaType(name = "anyURI")
    protected String link;
    @XmlElement(required = true)
    protected NewsItems newsItems;

    /**
     * Gets the value of the title property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the value of the title property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTitle(String value) {
        this.title = value;
    }

    /**
     * Gets the value of the description property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the value of the description property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescription(String value) {
        this.description = value;
    }

    /**
     * Gets the value of the link property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLink() {
        return link;
    }

    /**
     * Sets the value of the link property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLink(String value) {
        this.link = value;
    }

    /**
     * Gets the value of the newsItems property.
     * 
     * @return
     *     possible object is
     *     {@link NewsItems }
     *     
     */
    public NewsItems getNewsItems() {
        return newsItems;
    }

    /**
     * Sets the value of the newsItems property.
     * 
     * @param value
     *     allowed object is
     *     {@link NewsItems }
     *     
     */
    public void setNewsItems(NewsItems value) {
        this.newsItems = value;
    }

}
