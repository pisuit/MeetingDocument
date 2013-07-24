package md.datamodel;

import java.util.List;

import javax.faces.model.ListDataModel;

import md.model.MeetingMember;

import org.primefaces.model.SelectableDataModel;

public class MemberDataModel extends ListDataModel<MeetingMember> implements SelectableDataModel<MeetingMember>{
	
	public MemberDataModel(){
		
	}
	
	public MemberDataModel(List<MeetingMember> data){
		super(data);
	}

	@Override
	public MeetingMember getRowData(String arg0) {
		// TODO Auto-generated method stub
		List<MeetingMember> memebrs = (List<MeetingMember>) getWrappedData();
		
		for(MeetingMember member : memebrs){
			if(member.getPersonalInfo().getSTAFFCODE().equals(arg0)){
				return member;
			}		
		}
		return null;
	}

	@Override
	public Object getRowKey(MeetingMember arg0) {
		// TODO Auto-generated method stub
		return arg0.getPersonalInfo().getSTAFFCODE();
	}
}
