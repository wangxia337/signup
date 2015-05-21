package com.xwang.maven.account.persist;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentFactory;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import java.io.*;
import java.util.List;

public class AccountPersistServiceImpl implements AccountPersistService{

    private static final String ELEMENT_ROOT = "account-persist";
    private static final String ELEMENT_ACCOUNTS = "accounts";
    private static final String ELEMENT_ACCOUNT_ID = "id";
    private static final String ELEMENT_ACCOUNT_EMAIL = "email";
    private static final String ELEMENT_ACCOUNT_NAME = "name";
    private static final String ELEMENT_ACCOUNT_PASSWORD = "password";
    private static final String ELEMENT_ACCOUNT_ACTIVATED = "activated";
    private static final String ELEMENT_ACCOUNT = "account";

    private String file;

    private SAXReader reader = new SAXReader();

    public Account createAccount(Account account) throws AccountPersistException {
        Document doc = readDocument();
        Element accountsEle = doc.getRootElement().element(ELEMENT_ACCOUNTS);
        Element accountEle = accountsEle.addElement(ELEMENT_ACCOUNT);
        accountEle.addElement(ELEMENT_ACCOUNT_ID).addText(account.getId());
        accountEle.addElement(ELEMENT_ACCOUNT_NAME).addText(account.getName());
        accountEle.addElement(ELEMENT_ACCOUNT_EMAIL).addText(account.getEmail());
        accountEle.addElement(ELEMENT_ACCOUNT_PASSWORD).addText(account.getPassword());
        accountEle.addElement(ELEMENT_ACCOUNT_ACTIVATED).addText(String.valueOf(account.isActivated()));
        writeDocument(doc);
        return null;
    }

    public Account readAccount(String id) throws AccountPersistException {
        Document doc = readDocument();
        
        Element accountsEle = doc.getRootElement().element(ELEMENT_ACCOUNTS);
        
        for(Element accountEle : (List<Element>) accountsEle.elements()){
            if(accountEle.elementText(ELEMENT_ACCOUNT_ID).equals(id)){
                return buildAccount(accountEle);
            }
        }
        return null;
    }

    public Account updateAccount(Account account) throws AccountPersistException {
        Document doc = readDocument();
        Element accountEle = doc.getRootElement().element(ELEMENT_ACCOUNTS).element(ELEMENT_ACCOUNT);
        updateAccountId(account, accountEle);
        updateAccountName(account, accountEle);
        updateAccountEmail(account, accountEle);
        updateAccountPassword(account, accountEle);
        updateAccountStatus(account, accountEle);
        writeDocument(doc);
        return account;
    }

    public void deleteAccount(String id) throws AccountPersistException {
        Document doc = readDocument();

        Element accountsEle = doc.getRootElement().element(ELEMENT_ACCOUNTS);
        for(Element accountEle : (List<Element>) accountsEle.elements()){
            if(accountEle.elementText(ELEMENT_ACCOUNT_ID).equals(id)){
                accountsEle.remove(accountEle);
                break;
            }
        }
        writeDocument(doc);
    }

    private Account buildAccount(Element accountEle) {
        Account account = new Account();

        account.setId(accountEle.elementText(ELEMENT_ACCOUNT_ID));
        account.setName(accountEle.elementText(ELEMENT_ACCOUNT_NAME));
        account.setEmail(accountEle.elementText(ELEMENT_ACCOUNT_EMAIL));
        account.setPassword(accountEle.elementText(ELEMENT_ACCOUNT_PASSWORD));
        account.setActivated("true".equals(accountEle.elementText(ELEMENT_ACCOUNT_ACTIVATED)));
        return account;
    }

    private void updateAccountStatus(Account account, Element accountEle) {
        accountEle.remove(accountEle.element(ELEMENT_ACCOUNT_ACTIVATED));
        accountEle.addElement(ELEMENT_ACCOUNT_ACTIVATED).addText(String.valueOf(account.isActivated()));
    }

    private void updateAccountPassword(Account account, Element accountEle) {
        accountEle.remove(accountEle.element(ELEMENT_ACCOUNT_PASSWORD));
        accountEle.addElement(ELEMENT_ACCOUNT_PASSWORD).addText(account.getPassword());
    }

    private void updateAccountEmail(Account account, Element accountEle) {
        accountEle.remove(accountEle.element(ELEMENT_ACCOUNT_EMAIL));
        accountEle.addElement(ELEMENT_ACCOUNT_EMAIL).addText(account.getEmail());
    }

    private void updateAccountName(Account account, Element accountEle) {
        accountEle.remove(accountEle.element(ELEMENT_ACCOUNT_NAME));
        accountEle.addElement(ELEMENT_ACCOUNT_NAME).addText(account.getName());
    }

    private void updateAccountId(Account account, Element accountEle) {
        accountEle.remove(accountEle.element(ELEMENT_ACCOUNT_ID));
        accountEle.addElement(ELEMENT_ACCOUNT_ID).addText(account.getId());
    }

    private Document readDocument() throws AccountPersistException {
        File dataFile = new File(file);

        if(!dataFile.exists()) {
            createDocument(dataFile);
        }

        try {
            return reader.read(new File(file));
        } catch (DocumentException e) {
            throw new AccountPersistException("Unable to read persist data xml", e);
        }
    }

    private void createDocument(File dataFile) throws AccountPersistException {
        dataFile.getParentFile().mkdirs();
        Document doc = DocumentFactory.getInstance().createDocument();
        Element rootElement = doc.addElement(ELEMENT_ROOT);
        rootElement.addElement(ELEMENT_ACCOUNTS);
        writeDocument(doc);
    }

    private void writeDocument(Document doc) throws AccountPersistException{
        Writer out = null;

        try{
            out = new OutputStreamWriter(new FileOutputStream(file), "utf-8");
            XMLWriter writer = new XMLWriter(out, OutputFormat.createPrettyPrint());
            writer.write(doc);
        } catch (IOException e) {
            throw new AccountPersistException("Unable to write persist data xml", e);
        } finally {
            if(out != null){
                try {
                    out.close();
                } catch (IOException e) {
                    throw new AccountPersistException("Unable to close persist data xml", e);
                }
            }
        }
    }

    public void setFile(String file) {
       this.file = file;
    }
}
