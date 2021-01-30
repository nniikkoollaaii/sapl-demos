package io.sapl.playground.views.main;

import java.util.Arrays;
import java.util.Optional;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.TabVariant;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.theme.Theme;

import io.sapl.playground.models.ExamplesEnum;
import io.sapl.playground.views.content.ContentView;
import io.sapl.playground.views.main.MainView;

/**
 * The main view is a top-level placeholder for other views.
 */
@CssImport(value = "./styles/views/main/main-view.css", themeFor = "vaadin-app-layout")
@CssImport("./styles/views/main/main-view.css")
//@PWA(name = "SAPL Playground", shortName = "SAPL", enableInstallPrompt = false)
@JsModule("./styles/shared-styles.js")
public class MainView extends AppLayout {

    public MainView() {
        HorizontalLayout header = createHeader();
        addToNavbar(header);
    }
 
    private HorizontalLayout createHeader() {
        HorizontalLayout header = new HorizontalLayout();
        header.setPadding(false);
        header.setSpacing(false);
        header.setWidthFull();        
        header.setAlignItems(FlexComponent.Alignment.CENTER);
        header.setId("header");
        
        Image logo = new Image("images/logo-header.png", "SAPL Logo");
        logo.setId("logo");
        header.add(logo);
        
        header.add(new H1("SAPL Playground"));
        
        //VerticalLayout vertical = new VerticalLayout();
        //vertical.setAlignItems(Alignment.END);
        
        Div buttons = new Div();
        buttons.setClassName("alignRight");
        
        Anchor linkToDocs = new Anchor("https://sapl.io/sapl-reference.html", "Docs");
        linkToDocs.setId("linkToDocsButton");
        buttons.add(linkToDocs);
        
        Select<String> select = new Select<>();
        select.setPlaceholder("Examples");
        select.setItems(Arrays.asList(ExamplesEnum.values()).stream().map(e -> e.getDisplayValue()));
        select.setId("dropdownButton");
        buttons.add(select);
        
        header.add(buttons);
        
        return header;
    }
}
