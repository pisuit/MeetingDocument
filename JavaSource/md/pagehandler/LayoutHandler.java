package md.pagehandler;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import md.model.PersonalInfo;
import md.utils.SessionUtils;

@ManagedBean(name="layoutHandler")
@RequestScoped
public class LayoutHandler {
	
	private String name;
	private PersonalInfo personalInfo;
	
	public boolean isLoggedIn(){
		PersonalInfo personalInfo = SessionUtils.getUserFromSession();
		if(personalInfo != null){
			return true;
		} else {
			return false;
		}
	}

	public String getName() {
		if(SessionUtils.getUserFromSession() != null){
			return SessionUtils.getUserFromSession().getTNAME()+" "+SessionUtils.getUserFromSession().getTSURNAME();
		}
		return null;
	}

	public PersonalInfo getPersonalInfo() {
		if(SessionUtils.getUserFromSession() != null){
			return SessionUtils.getUserFromSession();
		}
		return null;
	}
	
}
