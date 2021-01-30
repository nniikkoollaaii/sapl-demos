package io.sapl.playground.views.content;

import java.util.HashMap;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;

import io.sapl.api.interpreter.DocumentAnalysisResult;
import io.sapl.api.interpreter.InitializationException;
import io.sapl.api.interpreter.SAPLInterpreter;
import io.sapl.api.pdp.AuthorizationDecision;
import io.sapl.api.pdp.AuthorizationSubscription;
import io.sapl.interpreter.DefaultSAPLInterpreter;
import io.sapl.interpreter.EvaluationContext;
import io.sapl.interpreter.functions.AnnotationFunctionContext;
import io.sapl.interpreter.pip.AnnotationAttributeContext;
import io.sapl.playground.models.BasicExample;
import io.sapl.playground.models.Example;
import io.sapl.playground.views.main.MainView;
import io.sapl.vaadin.DocumentChangedEvent;
import io.sapl.vaadin.DocumentChangedListener;
import io.sapl.vaadin.Issue;
import io.sapl.vaadin.JsonEditor;
import io.sapl.vaadin.JsonEditorConfiguration;
import io.sapl.vaadin.SaplEditor;
import io.sapl.vaadin.SaplEditorConfiguration;
import io.sapl.vaadin.ValidationFinishedEvent;

@Route(value = "", layout = MainView.class)
@PageTitle("SAPL Playground")
@RouteAlias(value = "", layout = MainView.class)
@CssImport("./styles/views/content/content-view.css")
public class ContentView extends Div implements DocumentChangedListener {
	
	private SaplEditor saplEditor;
	private JsonEditor jsonEditor;
	private JsonEditor jsonOutput;
	
	private Paragraph jsonInputError;
	private Paragraph evaluationError;
	
	private SAPLInterpreter saplInterpreter;
	
    public ContentView() {
    	
    	this.saplInterpreter = new DefaultSAPLInterpreter();
    	
        setId("content-view");
        
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.setAlignItems(FlexComponent.Alignment.STRETCH);
        horizontalLayout.setId("dividePageHorizontal");
        
        horizontalLayout.add(createLeftSide());

        horizontalLayout.add(createRightSide());
		
        add(horizontalLayout);
        
        initalizeExample();
    }
    
	public void onDocumentChanged(DocumentChangedEvent event) {
		ObjectMapper objectMapper = new ObjectMapper();
		
		this.jsonInputError.setVisible(false);
		this.evaluationError.setVisible(false);
		
		String jsonInputString = jsonEditor.getDocument();
		JsonNode jsonInput = null;
		if(jsonInputString == null) {
			return;
		}
		try {
			jsonInput = objectMapper.readTree(jsonInputString);
		} catch (JsonProcessingException e) {
			this.jsonInputError.setVisible(true);
			e.printStackTrace();
			return;
		}
		
		
		String saplString = saplEditor.getDocument();
		if(saplString == null || saplString.isEmpty() || !this.saplInterpreter.analyze(saplString).isValid()) {
			this.evaluationError.setVisible(true);
			this.evaluationError.setText("Policy isn't valid!");
			System.out.println("Policy isn't valid!");
			return;
		}
		
    	var attributeCtx = new AnnotationAttributeContext();
		var functionCtx = new AnnotationFunctionContext();
		var variables = new HashMap<String, JsonNode>(1);
		var evaluationCtx = new EvaluationContext(attributeCtx, functionCtx, variables);
		
		AuthorizationSubscription authSub = new AuthorizationSubscription(
			jsonInput.findValue("subject"), 
			jsonInput.findValue("action"), 
			jsonInput.findValue("resource"), 
			jsonInput.findValue("environment")
		);
		
		AuthorizationDecision authDecision = this.saplInterpreter.evaluate(authSub, saplString, evaluationCtx).blockFirst();
		try {
			this.jsonOutput.setDocument(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(authDecision));
		} catch (JsonProcessingException e) {
			System.out.println(e);
			this.evaluationError.setVisible(true);
			this.evaluationError.setText("Error printing Evaluation Result!");
		}
		
	}
	
	private Component createLeftSide() {
        Div leftSide = new Div();
        leftSide.setId("leftSide");   
        
        SaplEditorConfiguration saplConfig = new SaplEditorConfiguration();
		saplConfig.setHasLineNumbers(true);
		saplConfig.setTextUpdateDelay(500);

		saplEditor = new SaplEditor(saplConfig);
		saplEditor.addDocumentChangedListener(this);
		saplEditor.addValidationFinishedListener(this::onValidationFinished);
		leftSide.add(saplEditor);
		
		return leftSide;
	}
	
	private Component createRightSide() {
		Div rightSide = new Div();
        rightSide.setId("rightSide");
        
        VerticalLayout rightSideVertical = new VerticalLayout();
        rightSideVertical.setId("rightSideVertical");
		rightSideVertical.add(createRightUpperSide());
		rightSideVertical.add(createRightLowerSide());
        rightSide.add(rightSideVertical);
        
        return rightSide;
	}
	
	private Component createRightUpperSide() {
        Div rightSideInputDiv = new Div();
        rightSideInputDiv.setId("rightSideInputDiv");
		
        Div jsonEditorDiv = new Div();
        jsonEditorDiv.setId("jsonEditorDiv");
		jsonEditor = new JsonEditor(new JsonEditorConfiguration());
		jsonEditor.addDocumentChangedListener(this);
		jsonEditorDiv.add(jsonEditor);
		rightSideInputDiv.add(jsonEditorDiv);
		
		this.jsonInputError = new Paragraph("Input JSON is not valid");
		this.jsonInputError.setVisible(false);
		this.jsonInputError.setClassName("errorText");
		rightSideInputDiv.add(this.jsonInputError);
		
		return rightSideInputDiv;
	}
	
	private Component createRightLowerSide() {
        Div rightSideOutputDiv = new Div();
        rightSideOutputDiv.setId("rightSideOutputDiv");

        Div jsonOutputDiv = new Div();
        jsonOutputDiv.setId("jsonOutputDiv");
        jsonOutput = new JsonEditor(new JsonEditorConfiguration());
		jsonOutputDiv.add(jsonOutput);
        rightSideOutputDiv.add(jsonOutputDiv);
        
		this.evaluationError = new Paragraph();
		this.evaluationError.setVisible(false);
		this.evaluationError.setClassName("errorText");
		rightSideOutputDiv.add(this.evaluationError);
		
		return rightSideOutputDiv;
	}
	

	private void onValidationFinished(ValidationFinishedEvent event) {
		System.out.println("validation finished");
		Issue[] issues = event.getIssues();
		System.out.println("issue count: " + issues.length);
		for (Issue issue : issues) {
			System.out.println(issue.getDescription());
		}
	}
	
	 private void initalizeExample() {
		 Example initExample = new BasicExample();
		 this.saplEditor.setDocument(initExample.getPolicy());
		 this.jsonEditor.setDocument(initExample.getAuthSub());
	 }

}
