package md.servicedao;

import java.util.Date;
import java.util.List;

import md.model.Category;
import md.model.Document;
import md.model.Log;
import md.model.Meeting;
import md.model.MeetingMember;
import md.model.PersonalInfo;
import md.model.Photo;
import md.utils.HibernateUtil;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

public abstract class GeneralDAO {
	public static List<String> getCategoryListAsString(){
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;
		
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
			
			List<String> categoryList = session.createQuery(
					"SELECT category.name " +
					"FROM Category category " +
					"WHERE category.dataStatus = 'NORMAL' " +
					"ORDER BY category.ordering ")
					.list();
			tx.commit();
			
			return categoryList;
		} catch (Exception e) {
			e.printStackTrace();
			if (tx != null) {
				tx.rollback();
			}
			return null;
		} finally {
			session.clear();
			session.close();
		}
	}
	
	public static List<Category> getCategoryList_M(){
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;
		
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
			
			List<Category> categoryList = session.createQuery(
					"SELECT distinct category " +
					"FROM Category category " +
					"left join fetch category.meetingList meetings " +
					"WHERE category.dataStatus = 'NORMAL' " +
					"ORDER BY category.ordering ")
					.list();
			tx.commit();
			
			return categoryList;
		} catch (Exception e) {
			e.printStackTrace();
			if (tx != null) {
				tx.rollback();
			}
			return null;
		} finally {
			session.clear();
			session.close();
		}
	}
	
	public static List<Log> getLogList(Date start, Date end){
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;
		
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
			
			List<Log> logList = session.createQuery(
					"SELECT distinct log " +
					"FROM Log log " +
					"left join fetch log.staff staff " +
					"left join fetch staff.employeeInfos emp " +
					"left join fetch emp.profession pro " +
					"WHERE trunc(log.timeStamp) >= :pstart " +
					"AND trunc(log.timeStamp) <= :pend " +
					"AND staff.id != null " +
					"AND emp != null " +
					"AND pro.status = 'C' " +
					"ORDER BY log.timeStamp desc")
					.setParameter("pstart", start)
					.setParameter("pend", end)
					.list();
			
			tx.commit();
			return logList;		
		} catch (Exception e) {
			e.printStackTrace();
			if (tx != null) {
				tx.rollback();
			}
			return null;
		} finally {
			session.clear();
			session.close();
		}
	}
	
	public static List<Category> getCategoryList(){
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;

		try {
			session = sf.openSession();
			tx = session.beginTransaction();
			
			List<Category> categoryList = session.createQuery(
					"SELECT distinct category " +
					"FROM Category category " +
					"WHERE category.dataStatus = 'NORMAL' " +
					"ORDER BY category.ordering ")
					.list();
			tx.commit();
			
			return categoryList;
		} catch (Exception e) {
			e.printStackTrace();
			if (tx != null) {
				tx.rollback();
			}
			return null;
		} finally {
			session.clear();
			session.close();
		}
	}
	
	public static List<Meeting> getMeetingList(Category category){
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;
		
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
			
			List<Meeting> meetingList = session.createQuery(
					"SELECT distinct meeting " +
					"FROM Meeting meeting " +
					"left join fetch meeting.category " +
					"left join fetch meeting.members mem " +
					"left join fetch mem.personalInfo person " +
					"left join fetch person.employeeInfos emp " +
					"left join fetch emp.profession pro " +
					"WHERE meeting.dataStatus = 'NORMAL' " +
					"AND meeting.category = :pcategory " +
					"AND emp != null " +
					"AND pro.status = 'C' " +
					"ORDER BY meeting.ordering")
					.setParameter("pcategory", category)
					.list();
			tx.commit();
			
			return meetingList;
		} catch (Exception e) {
			e.printStackTrace();
			if (tx != null) {
				tx.rollback();
			}
			return null;
		} finally {
			session.clear();
			session.close();
		}
	}
	
	public static List<String> getMeetingListAsString(Category category){
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;
		
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
			
			List<String> meetingList = session.createQuery(
					"SELECT meeting.description " +
					"FROM Meeting meeting " +
					"WHERE meeting.dataStatus = 'NORMAL' " +
					"AND meeting.category = :pcategory " +
					"ORDER BY meeting.ordering")
					.setParameter("pcategory", category)
					.list();
			tx.commit();
			
			return meetingList;
		} catch (Exception e) {
			e.printStackTrace();
			if (tx != null) {
				tx.rollback();
			}
			return null;
		} finally {
			session.clear();
			session.close();
		}
	}
	
	public static Meeting getMeeting(Long meeting){
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;
		
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
			
			Meeting meet = (Meeting)session.createQuery(
					"SELECT distinct meeting " +
					"FROM Meeting meeting " +
					"WHERE meeting.id = :pmeet")
					.setParameter("pmeet", meeting)
					.uniqueResult();
			tx.commit();
			
			return meet;
		} catch (Exception e) {
			e.printStackTrace();
			if (tx != null) {
				tx.rollback();
			}
			return null;
		} finally {
			session.clear();
			session.close();
		}
	}
	
	public static Category getCategory(String name){
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;
		
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
			
			Category category = (Category) session.createQuery(
					"SELECT category " +
					"FROM Category category " +
					"WHERE category.name = :pname " +
					"AND category.dataStatus = 'NORMAL' ")
					.setParameter("pname", name)
					.uniqueResult();
			tx.commit();
			
			return category;
		} catch (Exception e) {
			e.printStackTrace();
			if (tx != null) {
				tx.rollback();
			}
			return null;
		} finally {
			session.clear();
			session.close();
		}
	}
	
	public static Category getCategory(Category cat){
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;
		
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
			
			Category category = (Category) session.createQuery(
					"SELECT category " +
					"FROM Category category " +
					"WHERE category = :pcat " +
					"AND category.dataStatus = 'NORMAL' ")
					.setParameter("pcat", cat)
					.uniqueResult();
			tx.commit();
			
			return category;
		} catch (Exception e) {
			e.printStackTrace();
			if (tx != null) {
				tx.rollback();
			}
			return null;
		} finally {
			session.clear();
			session.close();
		}
	}
	
	public static Category saveCategory(Category category){
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;
		
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
			
			session.saveOrUpdate(category);
			tx.commit();
			
			return category;
		} catch (Exception e) {
			e.printStackTrace();
			if (tx != null) {
				tx.rollback();
			}
			return null;
		} finally {
			session.clear();
			session.close();
		}
	}
	
	public static Meeting saveMeeting(Meeting meeting, List<MeetingMember> members){
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;
		
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
			
			session.saveOrUpdate(meeting);
			if(members != null){
				for(MeetingMember member : GeneralDAO.getMemberList(meeting)){
					session.delete(member);
				}
		
				for(MeetingMember member : members){
					member.setMeeting(meeting);
					session.saveOrUpdate(member);
				}
			}
			
			tx.commit();
			return meeting;
		} catch (Exception e) {
			e.printStackTrace();
			if (tx != null) {
				tx.rollback();
			}
			return null;
		} finally {
			session.clear();
			session.close();
		}
	}
	
	public static Document saveDocument(Document document){
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;
		
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
			
			session.saveOrUpdate(document);
			
			tx.commit();
			
			return document;
		} catch (Exception e) {
			e.printStackTrace();
			if (tx != null) {
				tx.rollback();
			}
			return null;
		} finally {
			session.clear();
			session.close();
		}
	}
	
	public static List<PersonalInfo> getEmployeeList(){
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;
		
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
			
			List<PersonalInfo> personalList = session.createQuery(
					"SELECT distinct personal " +
					"FROM PersonalInfo personal " +
					"left join fetch personal.employeeInfos emp " +
					"left join fetch emp.profession pro " +
					"WHERE emp != null " +
					"AND pro.status = 'C' " +
					"ORDER BY personal.TNAME ")
					.list();
			tx.commit();
			
			return personalList;
		} catch (Exception e) {
			e.printStackTrace();
			if (tx != null) {
				tx.rollback();
			}
			return null;
		} finally {
			session.clear();
			session.close();
		}
	}
	
	@SuppressWarnings("unchecked")
	public static List<Document> getRootDocumentsForMeetingASC(Meeting meeting){
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;
		
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
			
			List<Document> documents = session.createQuery(
					"SELECT distinct document " +
					"FROM Document document " +
					"left join fetch document.childFolder child " +
					"left join fetch document.rootFolder root " +
					"WHERE document.meeting = :pmeeting " +
					"AND document.dataStatus = 'NORMAL' " +
					"AND document.rootFolder = null " +
					"ORDER BY upper(document.fileName) asc, document.fileName asc")
					.setParameter("pmeeting", meeting)
					.list();
			
			tx.commit();
			
			return documents;
		} catch (Exception e) {
			e.printStackTrace();
			if (tx != null) {
				tx.rollback();
			}
			return null;
		} finally {
			session.clear();
			session.close();
		}
	}
	
	@SuppressWarnings("unchecked")
	public static List<Document> getRootDocumentsForMeetingDESC(Meeting meeting){
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;
		
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
			
			List<Document> documents = session.createQuery(
					"SELECT distinct document " +
					"FROM Document document " +
					"left join fetch document.childFolder child " +
					"left join fetch document.rootFolder root " +
					"WHERE document.meeting = :pmeeting " +
					"AND document.dataStatus = 'NORMAL' " +
					"AND document.rootFolder = null " +
					"ORDER BY upper(document.fileName) desc, document.fileName desc")
					.setParameter("pmeeting", meeting)
					.list();
			
			tx.commit();
			
			return documents;
		} catch (Exception e) {
			e.printStackTrace();
			if (tx != null) {
				tx.rollback();
			}
			return null;
		} finally {
			session.clear();
			session.close();
		}
	}
	
	public static Document getRootDocuments(Long doc){
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;
		
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
			
			Document document = (Document) session.createQuery(
					"SELECT distinct document " +
					"FROM Document document " +
					"left join fetch document.rootFolder " +
					"WHERE document.dataStatus = 'NORMAL' " +
					"AND document.id = :pdoc ")
					.setParameter("pdoc", doc)
					.uniqueResult();
			
			tx.commit();
			
			return document;
		} catch (Exception e) {
			e.printStackTrace();
			if (tx != null) {
				tx.rollback();
			}
			return null;
		} finally {
			session.clear();
			session.close();
		}
	}
	
	@SuppressWarnings("unchecked")
	public static List<Long> getViewList(){
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;
		
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
			
			List<Long> documents = session.createQuery(
					"SELECT distinct document.id " +
					"FROM Document document " +
					"WHERE document.dataStatus = 'NORMAL' ")
//					"AND document.isFolder = true ")
					.list();
			
			tx.commit();
			
			return documents;
		} catch (Exception e) {
			e.printStackTrace();
			if (tx != null) {
				tx.rollback();
			}
			return null;
		} finally {
			session.clear();
			session.close();
		}
	}
	
	@SuppressWarnings("unchecked")
	public static List<Document> getChildDocument(Document doc){
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;
		
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
			
			List<Document> documents = session.createQuery(
					"SELECT distinct document " +
					"FROM Document document " +
					"left join fetch document.childFolder child " +
					"WHERE document.rootFolder = :pdoc " +
					"AND document.dataStatus = 'NORMAL' " +
					"ORDER BY upper(document.fileName) desc, document.fileName desc")
					.setParameter("pdoc", doc)
					.list();
			
			tx.commit();
			
			return documents;
		} catch (Exception e) {
			e.printStackTrace();
			if (tx != null) {
				tx.rollback();
			}
			return null;
		} finally {
			session.clear();
			session.close();
		}
	}
	
	public static Document getDocument(String fileName, Document parent){
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;
		
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
			
			Document document = (Document) session.createQuery(
					"SELECT document " +
					"FROM Document document " +
					"left join fetch document.childFolder " +
					"left join fetch document.rootFolder root " +
					"WHERE root = :pparent " +
					"AND document.fileName = :pfilename " +
					"AND document.dataStatus = 'NORMAL' ")
					.setParameter("pparent", parent)
					.setParameter("pfilename", fileName)
					.uniqueResult();
			
			tx.commit();
			
			return document;
		} catch (Exception e) {
			e.printStackTrace();
			if (tx != null) {
				tx.rollback();
			}
			return null;
		} finally {
			session.clear();
			session.close();
		}
	}
	
	public static Document getDocument(Document doc){
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;
		
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
			
			Document document = (Document) session.createQuery(
					"SELECT distinct document " +
					"FROM Document document " +
					"left join fetch document.childFolder child " +
					"left join fetch document.rootFolder root " +
					"WHERE document.id = :pmeetingid " +
					"ORDER BY case child.isFolder when true then 1 else 2 end, upper(child.fileName) ")
					.setParameter("pmeetingid", doc.getId())
					.uniqueResult();
			
			tx.commit();
			
			return document;
		} catch (Exception e) {
			e.printStackTrace();
			if (tx != null) {
				tx.rollback();
			}
			return null;
		} finally {
			session.clear();
			session.close();
		}
	}
	
	public static Document getDocument(Long id){
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;
		
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
			
			Document document = (Document) session.createQuery(
					"SELECT document " +
					"FROM Document document " +
					"left join fetch document.childFolder child " +
					"left join fetch document.rootFolder " +
					"WHERE document.id = :pmeetingid " +
					"ORDER BY case child.isFolder when true then 1 else 2 end, upper(child.fileName)")
					.setParameter("pmeetingid", id)
					.uniqueResult();
			
			tx.commit();
			
			return document;
		} catch (Exception e) {
			e.printStackTrace();
			if (tx != null) {
				tx.rollback();
			}
			return null;
		} finally {
			session.clear();
			session.close();
		}
	}
	
	@SuppressWarnings("unchecked")
	public static List<MeetingMember> getMemberList(Meeting meeting){
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;
		
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
			
			List<MeetingMember> memberList = session.createQuery(
					"SELECT distinct mem " +
					"FROM MeetingMember mem " +
					"left join fetch mem.personalInfo person " +
					"left join fetch person.employeeInfos emp " +
					"left join fetch emp.profession pro " +
					"WHERE mem.meeting = :pmeeting " +
					"AND emp != null " +
					"AND pro.status = 'C' " +
					"ORDER BY case mem.isAdmin when true then 1 else 2 end desc, cast(pro.tceiling, int), cast(pro.lceiling, int),person.TNAME desc")
					.setParameter("pmeeting", meeting)
					.list();
			tx.commit();

			return memberList;
		} catch (Exception e) {
			e.printStackTrace();
			if (tx != null) {
				tx.rollback();
			}
			return null;
		} finally {
			session.clear();
			session.close();
		}
	}
	
	public static MeetingMember getBossMember(Meeting meeting, PersonalInfo person){
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;
		
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
			
			MeetingMember member = (MeetingMember) session.createQuery(
					"SELECT distinct mem " +
					"FROM MeetingMember mem " +
					"WHERE mem.meeting = :pmeeting " +
					"AND mem.personalInfo = :pperson")
					.setParameter("pmeeting", meeting)
					.setParameter("pperson", person)
					.uniqueResult();
			tx.commit();

			return member;
		} catch (Exception e) {
			e.printStackTrace();
			if (tx != null) {
				tx.rollback();
			}
			return null;
		} finally {
			session.clear();
			session.close();
		}
	}
	
	public static PersonalInfo getUser(String staffcode){
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;
		
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
			
			PersonalInfo personalInfo = (PersonalInfo) session.createQuery(
					"SELECT person " +
					"FROM PersonalInfo person " +
					"left join fetch person.employeeInfos emp " +
					"left join fetch emp.profession pro " +
					"WHERE person.STAFFCODE = :pstaffcode " +
					"AND emp != null " +
					"AND pro.status = 'C' ")
					.setParameter("pstaffcode", staffcode)
					.uniqueResult();
			tx.commit();
			
			return personalInfo;
		} catch (Exception e) {
			e.printStackTrace();
			if (tx != null) {
				tx.rollback();
			}
			return null;
		} finally {
			session.clear();
			session.close();
		}
	} 
	
	public static Photo getPhoto(String staffCode){
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;
		
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
			
			Photo photo = (Photo) session.createQuery(
					"SELECT photo " +
					"FROM Photo photo " +
					"WHERE photo.staffCode = :pstaffcode ")
					.setParameter("pstaffcode", staffCode)
					.uniqueResult();
			
			tx.commit();
			return photo;
		} catch (Exception e) {
			e.printStackTrace();
			if (tx != null) {
				tx.rollback();
			}
			return null;
		} finally {
			session.clear();
			session.close();
		}
	}
	
	public static void saveLog(Log log){
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;
		
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
			
			session.saveOrUpdate(log);
			
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (tx != null) {
				tx.rollback();
			}
		} finally {
			session.clear();
			session.close();
		}
	}
	
	public static int getNextOrderCategory(){
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;
		
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
			
			int max = (Integer) session.createQuery(
					"SELECT max(category.ordering) " +
					"FROM Category category " +
					"WHERE category.dataStatus = 'NORMAL' ")
					.uniqueResult();
			
			tx.commit();
			
			return max+1;
		} catch (Exception e) {
			e.printStackTrace();
			if (tx != null) {
				tx.rollback();
			}
			return 1;
		} finally {
			session.clear();
			session.close();
		}
	}
	
	public static int getNextOrderMeeting(Category category){
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;
		
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
			
			int max = (Integer) session.createQuery(
					"SELECT max(meeting.ordering) " +
					"FROM Meeting meeting " +
					"WHERE meeting.dataStatus = 'NORMAL' " +
					"AND meeting.category = :pcategory ")
					.setParameter("pcategory", category)
					.uniqueResult();
			
			tx.commit();
			
			return max+1;
		} catch (Exception e) {
			e.printStackTrace();
			if (tx != null) {
				tx.rollback();
			}
			return 1;
		} finally {
			session.clear();
			session.close();
		}
	}
	
	public static Meeting getMeetingByName(String name, Category category){
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;
		
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
			
			Meeting meeting = (Meeting) session.createQuery(
					"SELECT meeting " +
					"FROM Meeting meeting " +
					"WHERE meeting.description = :pname " +
					"AND meeting.dataStatus = 'NORMAL' " +
					"AND meeting.category  = :pcategory ")
					.setParameter("pname", name)
					.setParameter("pcategory", category)
					.uniqueResult();
			
			tx.commit();
			return meeting;
		} catch (Exception e) {
			e.printStackTrace();
			if (tx != null) {
				tx.rollback();
			}
			return null;
		} finally {
			session.clear();
			session.close();
		}
	}
	
	public static Category getCategoryByName(String name){
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;
		
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
			
			Category category = (Category) session.createQuery(
					"SELECT category " +
					"FROM Category category " +
					"WHERE category.name = :pname " +
					"AND category.dataStatus = 'NORMAL' ")
					.setParameter("pname", name)
					.uniqueResult();
			
			tx.commit();
			return category;
		} catch (Exception e) {
			e.printStackTrace();
			if (tx != null) {
				tx.rollback();
			}
			return null;
		} finally {
			session.clear();
			session.close();
		}
	}
}

	
