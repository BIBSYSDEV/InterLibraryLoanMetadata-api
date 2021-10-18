# Inter Library Loan Metadata API

The purpose of this project is to fetch metadata required for inter library loan by given parameters. 
Document id is sent as search parameter document_id.
The return value is expected to be a record of metadata about the given monography and a list of 
libraries that have an exemplar of the book.
Furthermore, the library is checked if it is part of Alma and has an ncip-server-url registered (at 
NB BaseBibliotek)
Finally, there is a POST endpoint to send in NCIP-orders to the extern library.

The application uses several AWS resources, including Lambda functions and an API Gateway API. These resources are 
defined in the `template.yaml` file in this project. You can update the template to add AWS resources through the same 
deployment process that updates your application code.

Prerequisites (shared resources):
* HostedZone: [sandbox|dev|test|prod].bibs.aws.unit.no
* Create a CodeStarConnection that allows CodePipeline to get events from and read the GitHub repository

  The user creating the connection must have permission to create "apps" i GitHub
* SSM Parameter Store Parameters:
  * /api/domainName = api.[sandbox|dev|test|prod].bibs.aws.unit.no
  * /github-connection = (CodeStarConnections ARN from above)
* Create the following CloudFormation stack manually using the AWS Web Console, CLI or API:
  * Stack for Custom Domain Name, Certificate and Route53 RecordSet:
    * Template: api-domain-name.yaml
    * Name: apigw-custom-domain-name-api-[sandbox|dev|test|prod]-bibs-aws-unit-no
    * Parameters:
      * HostedZoneId=[ID]

Bootstrap:
* Create the following CloudFormation stack manually using the AWS Web Console, CLI or API:
  * Stack for pipeline/CICD. This will bootstrap the app stack (template.yml)
    * Template: pipeline.yml
    * Name: interlibraryLoanMetadata-api
    * Parameters:
      * DeployStackName=interLibraryLoanMetadata-api-cf
      * GitBranch=develop
      * GitRepo=BIBSYSDEV/InterLibraryLoanMetadata-api
      * PipelineApprovalAction=[Yes|No] (No for non-prod?)
      * (Optional) PipelineApprovalEmail=[email address]


## Example

* GET to 

        /ill/metadata/?document_id=[documentID] 

Response:
```json
          {
  "isbn" : "0195213769",
  "source" : "BIBSYS_ILS",
  "record_id" : "BIBSYS_ILS71469955110002201",
  "publication_place" : "Boston",
  "b_title" : "The Handbook of social psychology : Vol. 1",
  "volume" : "Vol. 1",
  "creation_year" : "cop. 1998",
  "creator" : "",
  "pages" : "XX, 865",
  "publisher" : "McGraw-Hill ; distributed exclusively by Oxford University Press",
  "display_title" : "The Handbook of social psychology : Vol. 1",
  "libraries" : [ {
    "institution_code" : "HH",
    "display_name" : "Høgskolen i Innlandet Biblioteket Lillehammer",
    "mms_id" : "999919819107802214",
    "library_code" : "1050101",
    "available_for_loan" : true
  }, {
    "institution_code" : "HIT",
    "display_name" : "Universitetsbiblioteket i Sørøst-Norge Biblioteket - Ringerike",
    "mms_id" : "999919774625402210",
    "library_code" : "1060501",
    "available_for_loan" : true
  }, {
    "institution_code" : "NLA",
    "display_name" : "NLA Høgskolen Sandviken Biblioteket",
    "mms_id" : "999808823704702228",
    "library_code" : "1120134",
    "available_for_loan" : true
  }, {
    "institution_code" : "NTNU_UB",
    "display_name" : "NTNU Universitetsbiblioteket Biblioteket Dragvoll",
    "mms_id" : "999808823704702203",
    "library_code" : "1160106",
    "available_for_loan" : true
  }, {
    "institution_code" : "NHHB",
    "display_name" : "Norges handelshøyskole Biblioteket",
    "mms_id" : "999808823704702216",
    "library_code" : "1120125",
    "available_for_loan" : true
  }, {
    "institution_code" : "POLITIHS",
    "display_name" : "Politihøgskolen Biblioteket",
    "mms_id" : "999808823704702279",
    "library_code" : "1030217",
    "available_for_loan" : true
  }, {
    "institution_code" : "UBO",
    "display_name" : "UiO : Universitetsbiblioteket HumSam-biblioteket",
    "mms_id" : "999808823704702204",
    "library_code" : "1030300",
    "available_for_loan" : true
  }, {
    "institution_code" : "UBTO",
    "display_name" : "UiT Norges arktiske universitet Psykologi- og jusbiblioteket",
    "mms_id" : "999808823704702205",
    "library_code" : "1190203",
    "available_for_loan" : true
  }, {
    "institution_code" : "UBA",
    "display_name" : "Universitetsbiblioteket i Agder Grimstad",
    "mms_id" : "999808823704702209",
    "library_code" : "1090401",
    "available_for_loan" : true
  }, {
    "institution_code" : "UBB",
    "display_name" : "Universitetsbiblioteket i Bergen Bibliotek for samfunnsvitenskap, musikk og psykologi",
    "mms_id" : "999919848310402207",
    "library_code" : "1120108",
    "available_for_loan" : true
  }, {
    "institution_code" : "UBIS",
    "display_name" : "Universitetsbiblioteket i Stavanger Ullandhaug",
    "mms_id" : "999808823704702208",
    "library_code" : "1110301",
    "available_for_loan" : true
  } ]
}
   ```

* GET to

        /libcheck?libuser=[libuser]

Response:
```json
  {
      "isAlmaLibrary": true,
      "ncip_server_url": "https://bibsys.alma.exlibrisgroup.com/view/NcipP2PServlet"
  }
   ```

* POST to

        /ncip
  
  with a body that contains the xml-ncip message

Response:
```xml
  some ncip xml-response
   ```
