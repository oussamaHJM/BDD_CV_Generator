package miage.sgbd;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.itextpdf.io.IOException;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.AreaBreakType;

public class HelloWorld {
	
	// URL de connexion
		private String url = "jdbc:oracle:thin:@oracle.fil.univ-lille1.fr:1521:filora";
		// Nom du user
		private String user = "BAKKARI";
		// Mot de passe de l'utilisateur
		private String passwd = "M1MIAGE";
		// Objet Connection
		private static Connection connect;
		
		
		private HelloWorld() {
			try {
				connect = DriverManager.getConnection(this.url, this.user, this.passwd);
				connect.setAutoCommit(false);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		public static Connection getInstance() {
			if (connect == null) {
				new HelloWorld();
			}
			return connect;
		}
		
	public static void main(String[] args) throws IOException, SQLException, java.io.IOException {

	    File file = new File("ollee.pdf");
	    PdfWriter pdfWriter = new PdfWriter(file);
	    PdfDocument pdfDocument = new PdfDocument(pdfWriter);
	    Document document = new Document(pdfDocument);
	    //ImageData imageData = ImageDataFactory.create("moi.jpg");
	    //Image pdfImg = new Image(imageData);
	    ArrayList<String> Resultat_nom=new ArrayList<String>();
	    ArrayList<String> Resultat_prenom=new ArrayList<String>();
	    ArrayList<Blob> Resultat_photos=new ArrayList<Blob>();
	    ArrayList<String> Resultat_adresse=new ArrayList<String>();
	    ArrayList<String> Resultat_numero=new ArrayList<String>();
	    ArrayList<String> Resultat_email=new ArrayList<String>();

	    // Create Paragraphs
	    Connection c = getInstance() ;
	    Statement s2 = c.createStatement();
		//String sql = "select * from locmiage";
		String sql_etu = "select nom, prenom, extractvalue(cv,'/cv/donnees-personnelles/adresse/text()' ) as adresse, "
				+ "extractvalue(cv,'/cv/donnees-personnelles/telephone[@type=\"mobile\"]/text()' ) as numero,"
				+ "extractvalue(cv,'/cv/donnees-personnelles/e-mail/text()' ) as email from BOSSUT.vue_etu";
		//ResultSet rs = s2.executeQuery(sql);
		ResultSet rs_etu = s2.executeQuery(sql_etu);
		while (rs_etu.next()) {
			Resultat_nom.add(rs_etu.getString("Nom"));
			Resultat_prenom.add(rs_etu.getString("prenom"));
			Resultat_adresse.add(rs_etu.getString("adresse"));
			Resultat_numero.add(rs_etu.getString("numero"));
			Resultat_email.add(rs_etu.getString("email"));
		}
		
		for(int i=0; i<Resultat_nom.size();i++) {
			Paragraph paragraph = new Paragraph("Nom"+":"+"  "+Resultat_nom.get(i));
			Paragraph paragraph1 = new Paragraph("Prénom"+":"+"  "+Resultat_prenom.get(i));
			Paragraph paragraph2 = new Paragraph("adresse"+":"+"  "+Resultat_adresse.get(i));
			Paragraph paragraph3 = new Paragraph("numero"+":"+"  "+Resultat_numero.get(i));
			Paragraph paragraph4 = new Paragraph("email"+":"+"  "+Resultat_email.get(i));
			ImageData imageData = ImageDataFactory.create(Resultat_photos.get(i));
		    Image pdfImg = new Image(imageData);
		    pdfImg.setWidth(64);
		    pdfImg.setHeight(64);
			document.add(paragraph);
			document.add(paragraph1);
			document.add(paragraph2);
			document.add(paragraph3);
			document.add(paragraph4);
			//document.add(pdfImg);
			document.add(new AreaBreak(AreaBreakType.NEXT_PAGE));
		}
	    document.close();
	  }
}