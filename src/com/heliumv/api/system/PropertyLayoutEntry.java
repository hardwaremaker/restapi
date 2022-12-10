package com.heliumv.api.system;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlRootElement;

import com.heliumv.api.BaseEntryId;

@XmlRootElement
public class PropertyLayoutEntry extends BaseEntryId {
	
	private PropertyLayoutType type;
	private Integer row;
	private Integer column;
	private Integer height;
	private Integer paddingX;
	private Integer width;
	private Integer paddingY;
	private PropertyLayoutOrientation orientation;
	private PropertyLayoutFill fill;
	private BigDecimal weightX;
	private BigDecimal weightY;
	private Integer gapTop;
	private Integer gapRight;
	private Integer gapBottom;
	private Integer gapLeft;
	private Boolean mandatory;
	private Boolean heading;
	private String name;
	private String text;
	
	/**
	 * Art der Layoutkomponente
	 * @return
	 */
	public PropertyLayoutType getType() {
		return type;
	}
	public void setType(PropertyLayoutType type) {
		this.type = type;
	}
	
	/**
	 * Zeilennummer im Layout
	 * @return
	 */
	public Integer getRow() {
		return row;
	}
	public void setRow(Integer row) {
		this.row = row;
	}
	
	/**
	 * Spaltennummer im Layout
	 * @return
	 */
	public Integer getColumn() {
		return column;
	}
	public void setColumn(Integer column) {
		this.column = column;
	}
	
	/**
	 * H&ouml;he der Komponente
	 * @return
	 */
	public Integer getHeight() {
		return height;
	}
	public void setHeight(Integer height) {
		this.height = height;
	}
	
	/**
	 * Horizontales Padding der Komponente
	 * @return
	 */
	public Integer getPaddingX() {
		return paddingX;
	}
	public void setPaddingX(Integer paddingX) {
		this.paddingX = paddingX;
	}
	
	/**
	 * Breite der Komponente
	 * @return
	 */
	public Integer getWidth() {
		return width;
	}
	public void setWidth(Integer width) {
		this.width = width;
	}
	
	/**
	 * Vertikales Padding der Komponente
	 * @return
	 */
	public Integer getPaddingY() {
		return paddingY;
	}
	public void setPaddingY(Integer paddingY) {
		this.paddingY = paddingY;
	}
	
	/**
	 * Anordnung im Anzeigebereich
	 * @return
	 */
	public PropertyLayoutOrientation getOrientation() {
		return orientation;
	}
	public void setOrientation(PropertyLayoutOrientation orientation) {
		this.orientation = orientation;
	}
	
	/**
	 * F&uuml;llung im Anzeigebereich
	 * @return
	 */
	public PropertyLayoutFill getFill() {
		return fill;
	}
	public void setFill(PropertyLayoutFill fill) {
		this.fill = fill;
	}
	
	/**
	 * Horizontale Gewichtung
	 * @return
	 */
	public BigDecimal getWeightX() {
		return weightX;
	}
	public void setWeightX(BigDecimal weightX) {
		this.weightX = weightX;
	}
	
	/**
	 * Vertikale Gewichtung
	 * @return
	 */
	public BigDecimal getWeightY() {
		return weightY;
	}
	public void setWeightY(BigDecimal weightY) {
		this.weightY = weightY;
	}
	
	/**
	 * Abstand nach oben
	 * @return
	 */
	public Integer getGapTop() {
		return gapTop;
	}
	public void setGapTop(Integer gapTop) {
		this.gapTop = gapTop;
	}
	
	/**
	 * Abstand nach rechts
	 * @return
	 */
	public Integer getGapRight() {
		return gapRight;
	}
	public void setGapRight(Integer gapRight) {
		this.gapRight = gapRight;
	}
	
	/**
	 * Abstand nach unten
	 * @return
	 */
	public Integer getGapBottom() {
		return gapBottom;
	}
	public void setGapBottom(Integer gapBottom) {
		this.gapBottom = gapBottom;
	}
	
	/**
	 * Abstand nach links
	 * @return
	 */
	public Integer getGapLeft() {
		return gapLeft;
	}
	public void setGapLeft(Integer gapLeft) {
		this.gapLeft = gapLeft;
	}
	
	/**
	 * Ist diese Eigenschaft zwingend erforderlich?
	 * @return
	 */
	public Boolean getMandatory() {
		return mandatory;
	}
	public void setMandatory(Boolean mandatory) {
		this.mandatory = mandatory;
	}
	
	/**
	 * Ist dies Eigenschaft als &Uuml;berschrift definiert?
	 * @return
	 */
	public Boolean getHeading() {
		return heading;
	}
	public void setHeading(Boolean heading) {
		this.heading = heading;
	}
	
	/**
	 * Eindeutiger Name 
	 * @return
	 */
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Text der Komponente<br>
	 * Ist <code>type</code> ein <code>LABEL</code>, <code>CHECKBOX</code>, <code>PRINTBUTTON</code>
	 * oder <code>EXECUTEBUTTON</code> dann wird gepr&uuml;ft, ob der Text (als Token) im Ressoure-Bundle
	 * &uuml;bersetzt werden kann. Sonst wird der Text geliefert.
	 * Der Text ist speziellerweise zu interpretieren bei:
	 * <li>{@link PropertyLayoutType#CHECKBOX}: 0 .. die Checkbox ist gesetzt, oder 1 .. nicht gesetzt</li>
	 * <li>{@link PropertyLayoutType#COMBOBOX}: Die Auswahlm&ouml;glichkeiten sind mit einer Pipe &#124; getrennt.</li>
	 * 
	 * @return
	 */
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}

}
