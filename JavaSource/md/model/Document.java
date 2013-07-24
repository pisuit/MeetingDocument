package md.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import md.customtype.DataStatus;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="MDDOCUMENT")
@GenericGenerator(strategy="md.utils.HibernateCurrentTimeIDGenerator", name="IDGENERATOR")
public class Document {
		
	@Id
	@Column(name="ID")
	@GeneratedValue(generator="IDGENERATOR")
	private Long id;
	
	@Column(name="FILE_NAME")
	private String fileName;
	
	@Column(name="STORED_NAME")
	private String storedName;
	
	@Column(name="CONTENT_TYPE")
	private String contentType;
	
	@ManyToOne
	@JoinColumn(name="MEETING_ID", referencedColumnName="ID")
	private Meeting meeting;
	
	@Column(name="UPLOADTIME")
	private Date uploadTime;
	
	@Column(name="FILE_SIZE")
	private Long fileSize;
	
	@Column(name="DESCRIPTION")
	private String description;
	
	@ManyToOne
	@JoinColumn(name="ROOT_FOLDER_ID", referencedColumnName="ID")
	private Document rootFolder;
	
	@Column(name="ISFOLDER")
	private boolean isFolder;
	
	@Column(name="DATA_STATUS")
	@Enumerated(EnumType.STRING)
	private DataStatus dataStatus = DataStatus.NORMAL;
	
	@OneToMany(mappedBy="rootFolder")
	private List<Document> childFolder;
	
	@Transient
	private String fileType;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getStoredName() {
		return storedName;
	}

	public void setStoredName(String storedName) {
		this.storedName = storedName;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public Meeting getMeeting() {
		return meeting;
	}

	public void setMeeting(Meeting meeting) {
		this.meeting = meeting;
	}

	public Long getFileSize() {
		return fileSize;
	}

	public void setFileSize(Long fileSize) {
		this.fileSize = fileSize;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Document getRootFolder() {
		return rootFolder;
	}

	public void setRootFolder(Document rootFolder) {
		this.rootFolder = rootFolder;
	}

	public boolean isFolder() {
		return isFolder;
	}

	public void setFolder(boolean isFolder) {
		this.isFolder = isFolder;
	}

	public DataStatus getDataStatus() {
		return dataStatus;
	}

	public void setDataStatus(DataStatus dataStatus) {
		this.dataStatus = dataStatus;
	}

	public List<Document> getChildFolder() {
		return childFolder;
	}

	public void setChildFolder(List<Document> childFolder) {
		this.childFolder = childFolder;
	}

	public String getFileType() {
		if(contentType != null){
			String[] splitString = contentType.split("/");
			if(splitString[0].equals("application")){
				return splitString[1];
			} else {
				return contentType;
			}
		} else {
			return contentType;
		}
	}

	public Date getUploadTime() {
		return uploadTime;
	}

	public void setUploadTime(Date uploadTime) {
		this.uploadTime = uploadTime;
	}
}
