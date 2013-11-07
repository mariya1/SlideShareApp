package com.example.slideapp;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.example.weatherapp.R;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;

public class UserInfoActivity extends Activity {

	TextView authorTV;
	TextView titleTV;
	TextView pubDateTV;
	TextView guidTV;

	String author = "";
	String title = "";
	String pubDate = "";
	String guid = "";

	String yahooURLFirst = "http://query.yahooapis.com/v1/public/yql?q=SELECT%20*%20FROM%20slideshare.slideshows%20WHERE%20user%3D";
	String yahooURLSecond = "%20LIMIT%201%20&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_info);
		Intent intent = getIntent();
		String userSymbol = intent.getStringExtra(MainActivity.USER_SYMBOL);
		authorTV = (TextView) findViewById(R.id.author);
		titleTV = (TextView) findViewById(R.id.title);
		pubDateTV = (TextView) findViewById(R.id.pubDate);
		guidTV = (TextView) findViewById(R.id.linkTo);
		final String yqlURL = yahooURLFirst + "'" + userSymbol + "'"
				+ yahooURLSecond;
		new MyAsyncTask().execute(yqlURL);

	}

	private class MyAsyncTask extends AsyncTask<String, String, String> {
		protected String doInBackground(String... args) {
			try {
				URL url = new URL(args[0]);
				URLConnection connection;
				connection = url.openConnection();
				HttpURLConnection httpConnection = (HttpURLConnection) connection;
				int responseCode = httpConnection.getResponseCode();
				if (responseCode == HttpURLConnection.HTTP_OK) {
					InputStream in = httpConnection.getInputStream();
					DocumentBuilderFactory dbf = DocumentBuilderFactory
							.newInstance();
					DocumentBuilder db = dbf.newDocumentBuilder();
					Document dom = db.parse(in);
					Element docEle = dom.getDocumentElement();
					NodeList nl = docEle.getElementsByTagName("item");

					if (nl != null && nl.getLength() > 0) {
						for (int i = 0; i < nl.getLength(); i++) {
							UserInfo theUser = getUserInformation(docEle);
							author = theUser.getAuthor();
							title = theUser.getTitle();
							pubDate = theUser.getPubDate();
							guid = theUser.getGuid();

						}
					}
				}
			} catch (MalformedURLException e) {
			} catch (IOException e) {
			} catch (ParserConfigurationException e) {
			} catch (SAXException e) {
			} finally {
			}

			return null;
		}

		protected void onPostExecute(String result) {
			authorTV.setText("The latest publication by: " + author);
			titleTV.setText("Title: " + title);
			pubDateTV.setText("Published: " + pubDate);
			guidTV.setText("Direkt link to: " + guid);
		}

	}

	private UserInfo getUserInformation(Element entry) {

		String userName = getTextValue(entry, "author");
		String userTitle = getTextValue(entry, "title");
		String userPubDate = getTextValue(entry, "pubDate");
		String userGuid = getTextValue(entry, "guid");

		UserInfo theStock = new UserInfo(userName, userTitle, userPubDate,
				userGuid);

		return theStock;

	}

	private String getTextValue(Element entry, String tagName) {
		String tagValueToReturn = null;
		NodeList nl = entry.getElementsByTagName(tagName);
		if (nl != null && nl.getLength() > 0) {
			Element element = (Element) nl.item(0);
			tagValueToReturn = element.getFirstChild().getNodeValue();
		}
		return tagValueToReturn;
	}

}
