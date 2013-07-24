package md.model;

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

import md.customtype.DataStatus;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="MDMEETING")
@GenericGenerator(strategy="md.utils.HibernateCurrentTimeIDGenerator", name="IDGENERATOR")
public class Meeting {
	
	@Id
	@Column(name="ID")
	@GeneratedValue(generator="IDGENERATOR")
	private Long id;
	
	@Column(name="DESCRIPTION")
	private String description;
	
	@ManyToOne
	@JoinColumn(name="CATEGORY_ID", referencedColumnName="ID")
	private Category category;
	
	@Column(name="DATA_STATUS")
	@Enumerated(EnumType.STRING)
	private DataStatus dataStatus = DataStatus.NORMAL;
	
	@Column(name="ORDERING")
	private int ordering;
	
	@OneToMany(mappedBy="meeting")
	private List<MeetingMember> members;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Category getCategory() {
		return category;
	}
	public void setCategory(Category category) {
		this.category = category;
	}
	
	public int getOrdering() {
		return ordering;
	}
	public void setOrdering(int ordering) {
		this.ordering = ordering;
	}
	public DataStatus getDataStatus() {
		return dataStatus;
	}
	public void setDataStatus(DataStatus dataStatus) {
		this.dataStatus = dataStatus;
	}
	public List<MeetingMember> getMembers() {
		return members;
	}
	public void setMembers(List<MeetingMember> members) {
		this.members = members;
	}
	
	public String toString(){
		return description;
	}
}
