# Inter Library Loan Metadata API

The purpose of this project is to fetch metadata required for inter library loan by given parameters. 
Document id is sent as search parameter document_id.
The return value is expected to be a record of metadata about the given monography.

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
    * Name: alma-sru-proxy-cicd
    * Parameters:
      * DeployStackName=inter-library-loan-metadata
      * GitBranch=develop
      * GitRepo=BIBSYSDEV/InterLibraryLoanMetadata-api
      * PipelineApprovalAction=[Yes|No] (No for non-prod?)
      * (Optional) PipelineApprovalEmail=[email address]


## Example

* GET to 

        /interLibraryLoanMetadata/?document_id=[documentID] 

      

Either the isbn or the issn will contain a number but not both
  
     Response:
     ```json
          {
            "title": "Norges eldste medalje tildeles May-Britt Moser og Edvard Moser",
            "creator": "Ibsen, Henrik",
            "isbn": "",
            "issn": "",
            "publicationPlace": "",
            "year": "2006",
            "source": "BIBSYS_ILS - oria.no"
          }
     ```
  


