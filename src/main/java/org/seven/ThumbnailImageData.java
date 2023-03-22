package org.seven;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name="THUMBNAIL")
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class ThumbnailImageData {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "THUMBNAIL_SEQ")
  @SequenceGenerator(sequenceName = "thumb_seq", allocationSize = 1, name = "THUMBNAIL_SEQ")
  @Column(name = "ID")
  private Long id;
  @Lob
  @Column(name = "IMAGE_DATA")
  private byte[] imageData;
  @Lob
  @Column(name = "IMAGE_PROPERTY")
  private byte[] imageProperty;
}
