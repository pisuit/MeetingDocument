package md.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import md.customtype.LogType;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="MDLOG")
@GenericGenerator(strategy="md.utils.HibernateCurrentTimeIDGenerator", name="IDGENERATOR")
public class Log {
	
	@Id
	@Column(name="ID")
	@GeneratedValue(generator="IDGENERATOR")
	private Long id;
	
	@ManyToOne
	@JoinColumn(name="MEETING_ID", referencedColumnName="ID")
	private Meeting meeting;
	
	@ManyToOne
	@JoinColumn(name="STAFFCODE", referencedColumnName="STAFFCODE")
	private PersonalInfo staff;
	
	@Column(name="TIME_STAMP")
	private Date timeStamp;
	
	@Column(name="DETAIL")
	private String detail;
	
	@Column(name="LOG_TYPE")
	@Enumerated(EnumType.STRING)
	private LogType logType;
	
	public Log(){
		
	}
	
	public Log(Meeting meeting, PersonalInfo staff, Date timeStamp, String detail, LogType logType){
		this.meeting = meeting;
		this.staff = staff;
		this.timeStamp = timeStamp;
		this.detail = detail;
		this.logType = logType;
	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Meeting getMeeting() {
		return meeting;
	}
	public void setMeeting(Meeting meeting) {
		this.meeting = meeting;
	}
	public PersonalInfo getStaff() {
		return staff;
	}
	public void setStaff(PersonalInfo staff) {
		this.staff = staff;
	}
	public Date getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
	}
	public String getDetail() {
		return detail;
	}
	public void setDetail(String detail) {
		this.detail = detail;
	}
	public LogType getLogType() {
		return logType;
	}
	public void setLogType(LogType logType) {
		this.logType = logType;
	}
}
