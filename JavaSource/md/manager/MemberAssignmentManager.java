package md.manager;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import md.customtype.DataStatus;
import md.customtype.LogType;
import md.datamodel.MemberDataModel;
import md.model.Category;
import md.model.Document;
import md.model.EmployeeInfo;
import md.model.Log;
import md.model.Meeting;
import md.model.MeetingMember;
import md.model.PersonalInfo;
import md.servicedao.GeneralDAO;
import md.utils.BossUtil;
import md.utils.CalendarUtils;
import md.utils.CharacterUtils;
import md.utils.HibernateCurrentTimeIDGenerator;
import md.utils.PropertiesUtils;
import md.utils.SVNUtils;
import md.utils.SessionUtils;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.primefaces.component.tabview.TabView;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.NodeCollapseEvent;
import org.primefaces.event.NodeExpandEvent;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import org.primefaces.model.UploadedFile;
import org.tmatesoft.svn.core.SVNException;


@ManagedBean(name="memberAssign")
@ViewScoped
public class MemberAssignmentManager {
	private List<String> categoryCompleteList = new ArrayList<String>();
	private Category editCategory = new Category();
	private Meeting editMeeting = new Meeting();
	private MeetingMember editMeetingMember = new MeetingMember();
	private String selectedCategory;
	private List<MeetingMember> memberList = new ArrayList<MeetingMember>();
	private PersonalInfo selectedStaff;
	private MeetingMember deletedMember = new MeetingMember();
	private TreeNode categoryRootTree;
	private TreeNode selectedCategoryNode;
	private TreeNode documentRootTree;
	private TreeNode selectedDocumentNode;
	private String newFolderName;
	private String subFolderName;
	private String fileDescription;
	private MeetingMember[] selectedMembers;
	private MemberDataModel memberDataModel;
	private Document editDocument = new Document();
	private boolean isMeetingAdmin = false;
	private boolean isMeetingMember = false;
	private HashMap<Long, TreeNode> documentHashMap = new HashMap<Long, TreeNode>();
	private boolean isCopied = false;
	private HashMap<Long, Boolean> categoryStateHashMap = new HashMap<Long, Boolean>();
	private TreeNode moveDocumentTree;
	private TreeNode destinationNode;
	private List<Log> logList = new ArrayList<Log>();
	private Date startDate;
	private Date endDate;
	private List<SelectItem> logTypeFilterOptions = new ArrayList<SelectItem>();
	private boolean isFirstLevelAdmin = false;
	private List<String> orderList = new ArrayList<String>();
	private UploadedFile file;
	private String tabIndex = "0";
	private String ordering = "1";
	
	public List<String> getCategoryCompleteList() {
		return categoryCompleteList;
	}

	public void setCategoryCompleteList(List<String> categoryCompleteList) {
		this.categoryCompleteList = categoryCompleteList;
	}

	public Category getEditCategory() {
		return editCategory;
	}

	public void setEditCategory(Category editCategory) {
		this.editCategory = editCategory;
	}

	public boolean isMeetingAdmin() {
		return isMeetingAdmin;
	}

	public void setMeetingAdmin(boolean isMeetingAdmin) {
		this.isMeetingAdmin = isMeetingAdmin;
	}

	public Meeting getEditMeeting() {
		return editMeeting;
	}

	public void setEditMeeting(Meeting editMeeting) {
		this.editMeeting = editMeeting;
	}

	public MemberDataModel getMemberDataModel() {
		return memberDataModel;
	}

	public void setMemberDataModel(MemberDataModel memberDataModel) {
		this.memberDataModel = memberDataModel;
	}

	public boolean isFirstLevelAdmin() {
		return isFirstLevelAdmin;
	}

	public String getSubFolderName() {
		return subFolderName;
	}

	public void setSubFolderName(String subFolderName) {
		this.subFolderName = subFolderName;
	}

	public List<SelectItem> getLogTypeFilterOptions() {
		return logTypeFilterOptions;
	}

	public void setLogTypeFilterOptions(List<SelectItem> logTypeFilterOptions) {
		this.logTypeFilterOptions = logTypeFilterOptions;
	}

	public String getTabIndex() {
		return tabIndex;
	}

	public void setTabIndex(String tabIndex) {
		this.tabIndex = tabIndex;
	}

	public MeetingMember getEditMeetingMember() {
		return editMeetingMember;
	}

	public void setEditMeetingMember(MeetingMember editMeetingMember) {
		this.editMeetingMember = editMeetingMember;
	}

	public UploadedFile getFile() {
		return file;
	}

	public void setFile(UploadedFile file) {
		this.file = file;
	}

	public List<String> getOrderList() {
		return orderList;
	}

	public void setOrderList(List<String> orderList) {
		this.orderList = orderList;
	}

	public String getSelectedCategory() {
		return selectedCategory;
	}

	public void setSelectedCategory(String selectedCategory) {
		this.selectedCategory = selectedCategory;
	}

	public TreeNode getMoveDocumentTree() {
		return moveDocumentTree;
	}

	public void setMoveDocumentTree(TreeNode moveDocumentTree) {
		this.moveDocumentTree = moveDocumentTree;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public TreeNode getDestinationNode() {
		return destinationNode;
	}

	public void setDestinationNode(TreeNode destinationNode) {
		this.destinationNode = destinationNode;
	}

	public List<Log> getLogList() {
		return logList;
	}

	public void setLogList(List<Log> logList) {
		this.logList = logList;
	}

	public MeetingMember getDeletedMember() {
		return deletedMember;
	}

	public void setDeletedMember(MeetingMember deletedMember) {
		this.deletedMember = deletedMember;
	}

	public TreeNode getSelectedCategoryNode() {
		return selectedCategoryNode;
	}

	public void setSelectedCategoryNode(TreeNode selectedCategoryNode) {
		this.selectedCategoryNode = selectedCategoryNode;
	}

	public TreeNode getDocumentRootTree() {
		return documentRootTree;
	}

	public void setDocumentRootTree(TreeNode documentRootTree) {
		this.documentRootTree = documentRootTree;
	}

	public Document getEditDocument() {
		return editDocument;
	}

	public void setEditDocument(Document editDocument) {
		this.editDocument = editDocument;
	}

	public boolean isMeetingMember() {
		return isMeetingMember;
	}

	public void setMeetingMember(boolean isMeetingMember) {
		this.isMeetingMember = isMeetingMember;
	}

	public MeetingMember[] getSelectedMembers() {
		return selectedMembers;
	}

	public void setSelectedMembers(MeetingMember[] selectedMembers) {
		this.selectedMembers = selectedMembers;
	}

	public TreeNode getSelectedDocumentNode() {
		return selectedDocumentNode;
	}

	public void setSelectedDocumentNode(TreeNode selectedDocumentNode) {
		this.selectedDocumentNode = selectedDocumentNode;
	}

	public TreeNode getCategoryRootTree() {
		return categoryRootTree;
	}

	public void setCategoryRootTree(TreeNode categoryRootTree) {
		this.categoryRootTree = categoryRootTree;
	}

	public String getNewFolderName() {
		return newFolderName;
	}

	public void setNewFolderName(String newFolderName) {
		this.newFolderName = newFolderName;
	}

	public List<MeetingMember> getMemberList() {
		return memberList;
	}

	public void setMemberList(List<MeetingMember> memberList) {
		this.memberList = memberList;
	}
	
	public boolean isCopied() {
		return isCopied;
	}

	public String getFileDescription() {
		return fileDescription;
	}

	public void setFileDescription(String fileDescription) {
		this.fileDescription = fileDescription;
	}

	public PersonalInfo getSelectedStaff() {
		return selectedStaff;
	}

	public void setSelectedStaff(PersonalInfo selectedStaff) {
		this.selectedStaff = selectedStaff;
	}

	public MemberAssignmentManager(){
		startDate = new DateTime().now().dayOfMonth().withMinimumValue().toDateMidnight().toDate();
		endDate = new DateTime().now().dayOfMonth().withMaximumValue().toDateMidnight().toDate();
		createCategoryList();
		createCategoryTree();
		checkFirstLevelAdmin();
		createLogTypeFilterOptions();
	}
	
	private void checkFirstLevelAdmin(){
		if(SessionUtils.getUserFromSession() != null){
			String value = PropertiesUtils.getAdminProperties().getString("admin");
			String[] staffList = value.split(",");
			for(String str : staffList){
				if(str.equals(SessionUtils.getUserFromSession().getSTAFFCODE())){
					isFirstLevelAdmin = true;
				}
			}
		}
	}
	
	private void createCategoryList(){
		if(categoryCompleteList != null) categoryCompleteList.clear();
		
		categoryCompleteList.addAll(GeneralDAO.getCategoryListAsString());
	}
	
	private void createLogTypeFilterOptions(){
		if(logTypeFilterOptions != null) logTypeFilterOptions.clear();
		logTypeFilterOptions.add(new SelectItem("", "All"));
		
		for(LogType type : LogType.values()){
			logTypeFilterOptions.add(new SelectItem(type.getValue(), type.getValue()));
		}
	}
	
	public void createLogList(){
		if(logList != null) logList.clear();
		
		List<Log> list = GeneralDAO.getLogList(startDate, endDate);
		if(list != null){
			logList.addAll(list);
		}
//		logList.addAll(GeneralDAO.getLogList(startDate, endDate));
	}
	
	public List<String> categoryCompleteMethod(String input){
		List<String> output = new ArrayList<String>();
		for(String string : categoryCompleteList){
			if(StringUtils.contains(string, input)){
				output.add(string);
			}
		}
		return output;
	}
	
	public void saveAssignment(){
		Category category = GeneralDAO.getCategory(selectedCategory);
		if(category != null){
			editMeeting.setCategory(category);
			if(editMeeting.getId() == null){
				editMeeting.setOrdering(GeneralDAO.getNextOrderMeeting(category));
			}
		} else {
			Category newCategory = new Category();
			newCategory.setName(selectedCategory);
			newCategory.setOrdering(GeneralDAO.getNextOrderCategory());
			editMeeting.setCategory(GeneralDAO.saveCategory(newCategory));
			editMeeting.setOrdering(GeneralDAO.getNextOrderMeeting(newCategory));
		}
		int counter = 0;
		
		for(MeetingMember member : memberList){
			member.setAdmin(false);
			
			if(Arrays.asList(selectedMembers).contains(member)){
				member.setAdmin(true);
				memberList.set(counter, member);
			}
			counter++;
		}
		boolean isNew = true;
		if(editMeeting.getId() != null) {
			isNew = false;
		} 
		GeneralDAO.saveMeeting(editMeeting, memberList);
		if(isNew) {
			GeneralDAO.saveLog(new Log(editMeeting, SessionUtils.getUserFromSession(), CalendarUtils.getDateTimeInstance().getTime(), "Create Meeting", LogType.CREATE));
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success !!", "A new meeting has been created."));
		} else {
			GeneralDAO.saveLog(new Log(editMeeting, SessionUtils.getUserFromSession(), CalendarUtils.getDateTimeInstance().getTime(), "Edit Meeting Details", LogType.EDIT));
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success !!", "Meeting has been updated."));
		}
		resetValue();
		createCategoryTree();		
		createCategoryList();		
	}
	
	public void validateAssign(){
		List<FacesMessage> messageList = new ArrayList<FacesMessage>();
		if(selectedCategory.trim().length() == 0){
			messageList.add(new FacesMessage(FacesMessage.SEVERITY_WARN, "Missing Data !!", "Category"));
		}
		if(editMeeting.getDescription().trim().length() == 0){
			messageList.add(new FacesMessage(FacesMessage.SEVERITY_WARN, "Missing Data !!", "Meeting Name"));
		}
		if(memberList.size() == 0){
			messageList.add(new FacesMessage(FacesMessage.SEVERITY_WARN, "Missing Data !!", "Meeting Member"));
		}
		if(selectedMembers.length == 0){
			messageList.add(new FacesMessage(FacesMessage.SEVERITY_WARN, "Missing Data !!", "Meeting Admin"));
		}
		for(FacesMessage message : messageList){
			FacesContext.getCurrentInstance().addMessage(null, message);
		}
		if(messageList.size() == 0){
			saveAssignment();
			RequestContext.getCurrentInstance().execute("bar.hide()");
		}
	}
	
	public void changeOrdering(){
		createDocumentTree();
	}
	
	public void resetValue(){
		editMeeting = new Meeting();
		editMeetingMember = new MeetingMember();
		if(memberList != null) memberList.clear();
		selectedCategory = null;
		selectedStaff = null;
		isCopied = false;
		RequestContext.getCurrentInstance().execute("bar.hide()");
	}
	
	public void deleteMember(){
		int counter = 0;
		for(MeetingMember member : memberList){
			if(deletedMember.getPersonalInfo().getSTAFFCODE().equals(member.getPersonalInfo().getSTAFFCODE())){
				memberList.remove(counter);
				break;
			}
			counter++;
		}
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success !!", "Member has been deleted."));
		RequestContext.getCurrentInstance().execute("bar.show()");
	}
	
	public void saveMeetingMember(){
		if(selectedStaff != null){
			List<FacesMessage> messageList = new ArrayList<FacesMessage>();
			for(MeetingMember member : memberList){
				if(member.getPersonalInfo().staffEquals(selectedStaff)){				
					messageList.add(new FacesMessage(FacesMessage.SEVERITY_WARN, "Duplicate Data !!", "Member is already added."));				
				}
			}
			for(FacesMessage message : messageList){
				FacesContext.getCurrentInstance().addMessage(null, message);
			}
			if(messageList.size() == 0){
				MeetingMember newMember = new MeetingMember();
				newMember.setPersonalInfo(selectedStaff);
				memberList.add(newMember);
				memberDataModel = new MemberDataModel(memberList);
				selectedStaff = null;			
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success !!", "A new member has been added."));
				RequestContext.getCurrentInstance().execute("bar.show()");
			}		
		}
	}
	
	private void createCategoryTree(){
		TreeNode node1 = null;
		TreeNode node2 = null;
		categoryRootTree = new DefaultTreeNode("Root", null);
		for(Category category : GeneralDAO.getCategoryList()){
			node1 = new DefaultTreeNode(category, categoryRootTree);
			if(categoryStateHashMap.containsKey(category.getId()))
				node1.setExpanded(categoryStateHashMap.get(category.getId()));
//			node1.setSelectable(false);
			for(Meeting meeting : GeneralDAO.getMeetingList(category)){
				MeetingMember bossMember = GeneralDAO.getBossMember(meeting, BossUtil.getBoss());
				if(bossMember != null){
					bossMember.setPersonalInfo(BossUtil.getBoss());
					meeting.getMembers().add(bossMember);
				}			
				node2 = new DefaultTreeNode("meeting", meeting, node1);
			}
		}   
	}
	
	public void createDocumentTree(){
		if(selectedCategoryNode.getType().equals("meeting")){
			selectedDocumentNode = null;
			documentRootTree = new DefaultTreeNode("Root", null);
			List<Document> documents;
			if(ordering.equals("0")){
				documents = GeneralDAO.getRootDocumentsForMeetingASC((Meeting)selectedCategoryNode.getData());
			} else {
				documents = GeneralDAO.getRootDocumentsForMeetingDESC((Meeting)selectedCategoryNode.getData());
			}
		
			for(Document document : documents){
				TreeNode node = null;
				if(document.getDataStatus().equals(DataStatus.NORMAL)){
					if(document.isFolder() == true){				
						node = new DefaultTreeNode("folder", document, documentRootTree);
						documentHashMap.put(document.getId(), node);
					} else {
						node = new DefaultTreeNode("file", document, documentRootTree);
						documentHashMap.put(document.getId(), node);
					}
				}						
				TreeNode node2 = addChild(document, node, true);
			}
			prepareEditData();
			checkAdminRole();
		} else {
			selectedCategoryNode.setExpanded(true);
			categoryStateHashMap.put(((Category)selectedCategoryNode.getData()).getId(), true);
			editMeeting = new Meeting();
			selectedCategory = null;
			memberList.clear();
			documentRootTree = null;
			isCopied = false;
		}
	}
	
	public void prepareEditData(){
		selectedCategory = ((Meeting)selectedCategoryNode.getData()).getCategory().getName();
		editMeeting.setId(((Meeting)selectedCategoryNode.getData()).getId());
		editMeeting.setDescription(((Meeting)selectedCategoryNode.getData()).getDescription());
		editMeeting.setOrdering(((Meeting)selectedCategoryNode.getData()).getOrdering());

		if(memberList != null) memberList.clear();
		if(selectedMembers != null) selectedMembers = null;
		List<MeetingMember> tempMemList = new ArrayList<MeetingMember>();
//		tempMemList.addAll(((Meeting)selectedCategoryNode.getData()).getMembers());
		tempMemList.addAll(GeneralDAO.getMemberList((Meeting)selectedCategoryNode.getData()));

		MeetingMember bossMember = GeneralDAO.getBossMember((Meeting)selectedCategoryNode.getData(), BossUtil.getBoss());
	
//		Collections.sort(tempMemList, new Comparator<MeetingMember>() {
//
//			@Override
//			public int compare(MeetingMember o1, MeetingMember o2) {
//				// TODO Auto-generated method stub
//				return o1.getId().compareTo(o2.getId());
//			}
//			
//		});
		
		int counter = 0;
		int adminCounter = 0;
		for(MeetingMember member : tempMemList){
			member.setId(null);
			memberList.add(counter, member);
			if(member.isAdmin()) adminCounter++;
		}
		
		if(bossMember != null){
			bossMember.setPersonalInfo(BossUtil.getBoss());
			memberList.add(adminCounter, bossMember);
		}
		
		memberDataModel = new MemberDataModel(memberList);
		
		List<MeetingMember> selected = new ArrayList<MeetingMember>();
		for(MeetingMember member : memberList){	
			if(member.isAdmin()) selected.add(member);
		}
		
		selectedMembers = (MeetingMember[])selected.toArray(new MeetingMember[selected.size()]);
	}
	
	private TreeNode addChild(Document parentDoc, TreeNode parentNode, boolean isNew){
		TreeNode newNode = null;
		if(isNew){
			newNode = parentNode;
		} else {
			if(parentDoc.getDataStatus().equals(DataStatus.NORMAL)){
				if(parentDoc.isFolder() == true){
					newNode = new DefaultTreeNode("folder", parentDoc, parentNode);
					documentHashMap.put(parentDoc.getId(), newNode);
				} else {
					newNode = new DefaultTreeNode("file", parentDoc, parentNode);
					documentHashMap.put(parentDoc.getId(), newNode);
				}	
			}				
		}

//		for(Document document : GeneralDAO.getDocument(parentDoc).getChildFolder()){
		for(Document document : GeneralDAO.getChildDocument(parentDoc)){
			TreeNode newNode2 = addChild(document, newNode, false);
		}
		return newNode;
	}
	
	private void deleteDocumentTreeNode(Document doc){
		TreeNode deletedNode = documentHashMap.get(doc.getId());
		if(deletedNode != null){
			deletedNode.getChildren().clear();
			deletedNode.getParent().getChildren().remove(deletedNode);
			deletedNode.setParent(null);
			documentHashMap.remove(doc.getId());
		}
	}
	
	public void createNewFolder(){
		if(selectedCategoryNode != null){
			Document document = new Document();
			document.setFolder(true);
			document.setMeeting((Meeting)selectedCategoryNode.getData());
			document.setFileName(newFolderName);
			Document newDoc = GeneralDAO.saveDocument(document);
			GeneralDAO.saveLog(new Log(newDoc.getMeeting(), SessionUtils.getUserFromSession(), CalendarUtils.getDateTimeInstance().getTime(), "Create Folder "+"'"+newDoc.getFileName()+"'", LogType.CREATE));
			newFolderName = null;
			TreeNode node = new DefaultTreeNode("folder", newDoc, documentRootTree);
			documentHashMap.put(newDoc.getId(), node);
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success !!", "Folder has been created."));
		}
	}
	
	public void createSubFolder(){
		if(selectedDocumentNode != null){
			Document document = new Document();
			document.setFolder(true);
			document.setMeeting((Meeting)selectedCategoryNode.getData());
			document.setFileName(subFolderName);
			document.setRootFolder((Document)selectedDocumentNode.getData());
			Document newDoc = GeneralDAO.saveDocument(document);
			GeneralDAO.saveLog(new Log(newDoc.getMeeting(), SessionUtils.getUserFromSession(), CalendarUtils.getDateTimeInstance().getTime(), "Create Folder "+"'"+newDoc.getFileName()+"'", LogType.CREATE));
			subFolderName = null;
			TreeNode node = new DefaultTreeNode("folder", newDoc, selectedDocumentNode);
			selectedDocumentNode.setExpanded(true);
			documentHashMap.put(newDoc.getId(), node);
		}
	}
	
	public void preserveExpandState(NodeExpandEvent event){
		event.getTreeNode().setExpanded(true);
	}
	
	public void preserveCollapseState(NodeCollapseEvent event){
		event.getTreeNode().setExpanded(false);
	}
	
	public void handleFileUpload(FileUploadEvent event) throws IOException{	
		try {			
			String fileName;
			InputStream is;
			
			String[] splitted = event.getFile().getFileName().split("\\\\");
			
			if(splitted.length != 0)
				fileName = splitted[splitted.length-1];
			else fileName = event.getFile().getFileName();
			
			Document existDoc = GeneralDAO.getDocument(fileName, (Document)selectedDocumentNode.getData());
			if(existDoc == null){
				Document document = new Document();
				document.setFolder(false);
				document.setContentType(event.getFile().getContentType());
				document.setFileName(fileName);
				document.setMeeting((Meeting)selectedCategoryNode.getData());
				document.setStoredName(new HibernateCurrentTimeIDGenerator().generate(null, null).toString());
				document.setRootFolder((Document)selectedDocumentNode.getData());
				document.setFileSize(event.getFile().getSize());
				document.setUploadTime(CalendarUtils.getDateTimeInstance().getTime());
				document.setDescription(fileDescription);
				Document newDoc = GeneralDAO.saveDocument(document);
				GeneralDAO.saveLog(new Log(newDoc.getMeeting(), SessionUtils.getUserFromSession(), CalendarUtils.getDateTimeInstance().getTime(), "Upload "+"'"+newDoc.getFileName()+"'", LogType.UPLOAD));
				
				is = event.getFile().getInputstream();
				SVNUtils.uploadFile(is, newDoc.getStoredName());
				IOUtils.closeQuietly(is);
				
				TreeNode node = new DefaultTreeNode("file", newDoc, selectedDocumentNode);
				selectedDocumentNode.setExpanded(true);
				documentHashMap.put(newDoc.getId(), node);
			} else {
				existDoc.setDescription(fileDescription);
				existDoc.setUploadTime(CalendarUtils.getDateTimeInstance().getTime());
				existDoc.setFileSize(event.getFile().getSize());
				Document oldDoc = GeneralDAO.saveDocument(existDoc);
				GeneralDAO.saveLog(new Log(oldDoc.getMeeting(), SessionUtils.getUserFromSession(), CalendarUtils.getDateTimeInstance().getTime(), "Upload "+"'"+oldDoc.getFileName()+"'", LogType.UPLOAD));
				SVNUtils.deleteFile(existDoc.getStoredName());
				is = event.getFile().getInputstream();
				SVNUtils.uploadFile(is, oldDoc.getStoredName());
				IOUtils.closeQuietly(is);
				
				TreeNode deletedNode = documentHashMap.get(existDoc.getId());
				if(deletedNode != null){
					deletedNode.getChildren().clear();
					deletedNode.getParent().getChildren().remove(deletedNode);
					deletedNode.setParent(null);
					documentHashMap.remove(editDocument.getId());
				}
								
				TreeNode node = new DefaultTreeNode(existDoc, documentHashMap.get(existDoc.getRootFolder().getId()));
				documentHashMap.put(existDoc.getId(), node);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void download(Document doc) throws IOException{
		FacesContext fc = FacesContext.getCurrentInstance();
		ExternalContext ec = fc.getExternalContext();
		InputStream is = null;
		byte[] data;
		try {
			is = SVNUtils.downloadFile(doc.getStoredName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		data = IOUtils.toByteArray(is);
		
		ec.setResponseContentType(doc.getContentType());
		ec.setResponseContentLength(data.length);
		ec.setResponseCharacterEncoding("UTF-8");
		ec.setResponseHeader("Content-Disposition", "attachment;charset=utf-8; filename=\""+CharacterUtils.Unicode2ASCII(doc.getFileName()) + "\""); 
		
		try {
			OutputStream out = ec.getResponseOutputStream();
			out.write(data);

			fc.responseComplete();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}
	
	public void saveEditedDocument(){
		Document oldDoc = GeneralDAO.getDocument(editDocument);
		if(editDocument.isFolder()){
			Document doc =  GeneralDAO.saveDocument(editDocument);
			GeneralDAO.saveLog(new Log(doc.getMeeting(), SessionUtils.getUserFromSession(), CalendarUtils.getDateTimeInstance().getTime(), "Edit Folder Name "+"'"+oldDoc.getFileName()+"'"+" to "+"'"+doc.getFileName()+"'", LogType.EDIT));
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success !!", "Folder name has been modified."));
		} else {
			try {
				editDocument.setDescription(new String(editDocument.getDescription().getBytes("iso-8859-1"), "UTF-8"));
				editDocument.setFileName(new String(editDocument.getFileName().getBytes("iso-8859-1"), "UTF-8"));
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			if(file != null){
				editDocument.setContentType(file.getContentType());
				editDocument.setFileSize(file.getSize());
				editDocument.setUploadTime(CalendarUtils.getDateTimeInstance().getTime());
				
				InputStream is;
				try {
					SVNUtils.deleteFile(editDocument.getStoredName());
					is = file.getInputstream();
					SVNUtils.uploadFile(is, editDocument.getStoredName());
					IOUtils.closeQuietly(is);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SVNException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}			
			}
			
			Document doc =  GeneralDAO.saveDocument(editDocument);
			GeneralDAO.saveLog(new Log(doc.getMeeting(), SessionUtils.getUserFromSession(), CalendarUtils.getDateTimeInstance().getTime(), "Edit File Name/Description of "+"'"+doc.getFileName(), LogType.EDIT));			
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success !!", "File Name/Description has been modified."));			
		}
		file = null;
		editDocument = new Document();
		tabIndex = "0";
	}
	
	public void deleteDocument(){
		try {
			editDocument.setDataStatus(DataStatus.DELETED);
			Document doc =  GeneralDAO.saveDocument(editDocument);	
			GeneralDAO.saveLog(new Log(doc.getMeeting(), SessionUtils.getUserFromSession(), CalendarUtils.getDateTimeInstance().getTime(), "Delete Folder/File "+"'"+doc.getFileName()+"'", LogType.DELETE));
			if(editDocument.isFolder() == false){
				SVNUtils.deleteFile(editDocument.getStoredName());
			}
			deleteDocumentTreeNode(editDocument);
			getCHildNode(editDocument);
			if(editDocument.isFolder()){
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success !!", "Folder has been deleted."));
			} else {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success !!", "File has been deleted."));
			}
			editDocument = new Document();
		} catch (SVNException e) {
			e.printStackTrace();
//			 TODO: handle exception
		}
		
	}
	
	private void getCHildNode(Document parentDoc) throws SVNException{
		Document parent = GeneralDAO.getDocument(parentDoc);
		if(parent.getChildFolder() != null && parent.getChildFolder().size() != 0){
			for(Document doc : parent.getChildFolder()){
				if(!doc.getDataStatus().equals(DataStatus.DELETED)){
					doc.setDataStatus(DataStatus.DELETED);
					Document docs =  GeneralDAO.saveDocument(doc);
					GeneralDAO.saveLog(new Log(docs.getMeeting(), SessionUtils.getUserFromSession(), CalendarUtils.getDateTimeInstance().getTime(), "Delete Folder/File "+"'"+docs.getFileName()+"'", LogType.DELETE));
					if(doc.isFolder() == false){
						SVNUtils.deleteFile(doc.getStoredName());
					}
					deleteDocumentTreeNode(doc);
					getCHildNode(doc);
				}
			}
		}
	}	
	
	private void checkAdminRole(){
		for(MeetingMember mem : ((Meeting)selectedCategoryNode.getData()).getMembers()){
			if(SessionUtils.getUserFromSession() != null && SessionUtils.getUserFromSession().staffEquals(mem.getPersonalInfo())){
				isMeetingMember = true;
				if(mem.isAdmin() == true)
					isMeetingAdmin = true;
				break;
			}
			isMeetingMember = false;
			isMeetingAdmin = false;
		}
	}
	
	public void copyMeeting(){
		editMeeting.setId(null);
		isCopied = true;
	}
	
	public void setExpand(NodeExpandEvent event){
		event.getTreeNode().setExpanded(true);
		categoryStateHashMap.put(((Category)event.getTreeNode().getData()).getId(), true);
	}
	
	public void setCollapse(NodeCollapseEvent event){
		event.getTreeNode().setExpanded(false);
		categoryStateHashMap.put(((Category)event.getTreeNode().getData()).getId(), false);
	}
	
	public void treeTableSelected(){
		selectedDocumentNode.setExpanded(true);
	}
	
	public void prepareEditCategory(){
		if(selectedCategoryNode.getType().equals("default")){
			editCategory.setName(((Category)selectedCategoryNode.getData()).getName());
			editCategory.setId(((Category)selectedCategoryNode.getData()).getId());
		} else {
			
		}
	}
	
	public void saveEditedCategory(){
		Category oldCat = GeneralDAO.getCategory(editCategory);
		GeneralDAO.saveCategory(editCategory);
		GeneralDAO.saveLog(new Log(null, SessionUtils.getUserFromSession(), CalendarUtils.getDateTimeInstance().getTime(), "Edit Category Name From "+"'"+oldCat.getName()+"'"+" to "+"'"+editCategory.getName()+"'", LogType.EDIT));
		editCategory = new Category();
		createCategoryList();
		createCategoryTree();	
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success !!", "Category name has been modified."));
	}
	
	public void saveEditedMeeting(){
		Category category = GeneralDAO.getCategory(selectedCategory);
		editMeeting.setCategory(category);
		Meeting oldMeet = GeneralDAO.getMeeting(editMeeting.getId());
		Meeting meeting = GeneralDAO.saveMeeting(editMeeting, null);
		GeneralDAO.saveLog(new Log(meeting, SessionUtils.getUserFromSession(), CalendarUtils.getDateTimeInstance().getTime(), "Edit Meeting Name From "+"'"+oldMeet.getDescription()+"'"+" to "+"'"+meeting.getDescription()+"'", LogType.EDIT));
		editMeeting.setDescription(meeting.getDescription());
		createCategoryTree();
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success !!", "Meeting name has been modified."));
	}
	
	public void deleteCategory(){
		if(selectedCategoryNode.getType().equals("default")){		
			for(Meeting meeting : GeneralDAO.getMeetingList((Category)selectedCategoryNode.getData())){
				deleteMeeting(meeting);		
			}
			Category cat = ((Category)selectedCategoryNode.getData());
			cat.setDataStatus(DataStatus.DELETED);
			GeneralDAO.saveCategory(cat);
			GeneralDAO.saveLog(new Log(null, SessionUtils.getUserFromSession(), CalendarUtils.getDateTimeInstance().getTime(), "Delete Category "+"'"+cat.getName()+"'", LogType.DELETE));		
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success !!", "Category has been deleted."));
		} else {
			deleteMeeting((Meeting)selectedCategoryNode.getData());
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success !!", "Meeting has been deleted."));
		}
		selectedCategoryNode = null;
		documentRootTree = null;
		createCategoryTree();
		createCategoryList();
		resetValue();
	}
	
	private void deleteMeeting(Meeting meeting){
		List<Document> documents = GeneralDAO.getRootDocumentsForMeetingDESC(meeting);
		for(Document document : documents){
			try {
				document.setDataStatus(DataStatus.DELETED);
				GeneralDAO.saveDocument(document);	
				
				if(document.isFolder() == false){
					SVNUtils.deleteFile(document.getStoredName());
				}
				getCHildNode(document);
			} catch (SVNException e) {
				e.printStackTrace();
//				 TODO: handle exception
			}
		}
		meeting.setDataStatus(DataStatus.DELETED);
		Meeting meet = GeneralDAO.saveMeeting(meeting, null);
		GeneralDAO.saveLog(new Log(meet, SessionUtils.getUserFromSession(), CalendarUtils.getDateTimeInstance().getTime(), "Delete Meeting "+"'"+meet.getDescription()+"'", LogType.DELETE));		
	}
	
	public void createMoveDocumentTreeNode(){
		destinationNode = null;
		moveDocumentTree = new DefaultTreeNode("Root", null);
		List<Document> documents = GeneralDAO.getRootDocumentsForMeetingDESC((Meeting)selectedCategoryNode.getData());

		for(Document document : documents){
			TreeNode node = null;
			if(document.getDataStatus().equals(DataStatus.NORMAL)){
				if(document.isFolder() == true){				
					node = new DefaultTreeNode(document, moveDocumentTree);
				} 
			}						
			TreeNode node2 = addMoveChild(document, node, true);
		}
	}
	
	private TreeNode addMoveChild(Document parentDoc, TreeNode parentNode, boolean isNew){
		TreeNode newNode = null;
		if(isNew){
			newNode = parentNode;
		} else {
			if(parentDoc.getDataStatus().equals(DataStatus.NORMAL)){
				if(parentDoc.isFolder() == true){
					newNode = new DefaultTreeNode(parentDoc, parentNode);
				}
			}				
		}		 
//		for(Document document : GeneralDAO.getDocument(parentDoc).getChildFolder()){
		for(Document document : GeneralDAO.getChildDocument(parentDoc)){
			TreeNode newNode2 = addMoveChild(document, newNode, false);
		}
		return newNode;
	}
	
	public void expandMoveTree(){
		destinationNode.setExpanded(true);
	}
	
	public void moveDocument(){
		editDocument.setRootFolder((Document)destinationNode.getData());
		GeneralDAO.saveDocument(editDocument);
		GeneralDAO.saveLog(new Log(editDocument.getMeeting(), SessionUtils.getUserFromSession(), CalendarUtils.getDateTimeInstance().getTime(), "Move File "+"'"+editDocument.getFileName()+"'"+" From "+"'"+editDocument.getRootFolder().getFileName()+" to "+"'"+((Document)destinationNode.getData()).getFileName(), LogType.EDIT));		
		
		TreeNode deletedNode = documentHashMap.get(editDocument.getId());
		if(deletedNode != null){
			deletedNode.getChildren().clear();
			deletedNode.getParent().getChildren().remove(deletedNode);
			deletedNode.setParent(null);
			documentHashMap.remove(editDocument.getId());
		}
		
		
		TreeNode node = new DefaultTreeNode(editDocument, documentHashMap.get(((Document)destinationNode.getData()).getId()));
		expandAllParentAfterMoved(node.getParent());
		documentHashMap.put(editDocument.getId(), node);
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success !!", "File has been moved."));		
	}
	
	public void reserveMoveTreeExpandState(NodeExpandEvent event){
		event.getTreeNode().setExpanded(true);
	}
	
	public void reserveMoveTreeCollapseState(NodeCollapseEvent event){
		event.getTreeNode().setExpanded(false);
	}
	
	private void expandAllParentAfterMoved(TreeNode node){
		node.setExpanded(true);
		if(node.getParent() != null){
			expandAllParentAfterMoved(node.getParent());
		}
	}
	
	public void createOrderList(){
		if(orderList != null) orderList.clear();
		
		if(selectedCategoryNode.getType().equals("default")){
			orderList.addAll(GeneralDAO.getCategoryListAsString());
		} else {
			orderList.addAll(GeneralDAO.getMeetingListAsString(((Meeting)selectedCategoryNode.getData()).getCategory()));
		}
	}
	
	public void saveOrder(){
		int count = 1;
		if(selectedCategoryNode.getType().equals("default")){
			for(String name : orderList){
				Category category = GeneralDAO.getCategoryByName(name);
				if(category != null){
					category.setOrdering(count);
					GeneralDAO.saveCategory(category);
				}
				count++;
			}
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success !!", "Category has been reordered."));				
		} else {
			for(String name : orderList){
				Meeting meeting = GeneralDAO.getMeetingByName(name, ((Meeting)selectedCategoryNode.getData()).getCategory());
				if(meeting != null){
					meeting.setOrdering(count);
					GeneralDAO.saveMeeting(meeting, null);
				}
				count++;
			}
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success !!", "Meeting has been reordered."));
		}
		createCategoryTree();
	}

	public String getOrdering() {
		return ordering;
	}

	public void setOrdering(String ordering) {
		this.ordering = ordering;
	}
}
