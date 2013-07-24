package md.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import md.customtype.DataStatus;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="MDMEETINGMEMBER")
@GenericGenerator(strategy="md.utils.HibernateCurrentTimeIDGenerator", name="IDGENERATOR")
public class MeetingMember {
	
	@Id
	@Column(name="ID")
	@GeneratedValue(generator="IDGENERATOR")
	private Long id;
	
	@ManyToOne
	@JoinColumn(name="MEETING_ID", referencedColumnName="ID")
	private Meeting meeting;
	
	@ManyToOne
	@JoinColumn(name="MEMBER_ID", referencedColumnName="STAFFCODE")
	private PersonalInfo personalInfo;
	
	@Column(name="ISADMIN")
	private boolean isAdmin = false;
	
	@Column(name="DATA_STATUS")
	@Enumerated(EnumType.STRING)
	private DataStatus dataStatus = DataStatus.NORMAL;
	
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
	public PersonalInfo getPersonalInfo() {
		return personalInfo;
	}
	public void setPersonalInfo(PersonalInfo personalInfo) {
		this.personalInfo = personalInfo;
	}
	public boolean isAdmin() {
		return isAdmin;
	}
	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}
	public DataStatus getDataStatus() {
		return dataStatus;
	}
	public void setDataStatus(DataStatus dataStatus) {
		this.dataStatus = dataStatus;
	}
	@Override
	public boolean equals(Object obj){
		if((((MeetingMember)obj).getPersonalInfo().getSTAFFCODE().equals(personalInfo.getSTAFFCODE()))){
			return true;
		} else {
			return false;
		}
	}
}
