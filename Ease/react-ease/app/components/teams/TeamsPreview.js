import React from 'react';
import { NavLink } from 'react-router-dom';

function Step1(props){
  return (
      <div class="content display-flex marginAuto step1">
        <div class="display-flex flex_direction_column" style={{flexBasis:'500px', flexShrink:'0', marginRight:'100px'}}>
          <h3 style={{fontSize:'24px', marginBottom: '40px'}}>Vous pouvez maintenant gérer les mots de passe pour votre équipe sereinement et facilement.</h3>
          <span style={{lineHeight:'1.78', color: '#838593'}}>Des mots de passe mal gérés et des accès mal controlés impliquent une baisse de la sécurité et de la productivité de votre équipe.</span>
          <span style={{lineHeight:'1.78', color: '#838593'}}>Notre mission est de sécuriser la manière dont vous stockéz et partagez vos mots de passe tout en facilitant l'accès aux sites et outils web utilisés par vos collaborateurs.</span>
        </div>
        <div class="display-flex flex_direction_column justify_content_center" style={{flexBasis:'300px'}}>
          <img class="width100" src="/resources/images/Team.jpg" alt="image" style={{marginBottom: '20px'}}/>
          <button class="button-unstyle big-button"
                  style={{fontSize: '24px'}}
                  onClick={props.passStep.bind(null, true)}>
            Next
          </button>
        </div>
      </div>
  )
}

function TeamProPlan(props){
  return (
      <div class="team_plan" id="pro_team_plan" style={{marginRight: '40px'}}>
        <img src="/resources/other/illu.svg" alt="icon" class="styleImage"/>
        <h1 class="text-center title">Pro</h1>
        <span class="text-center price" style={{marginBottom: '33px'}}>3,99 <span class="symbol">€HT</span></span>
        <div class="text-center">
          <button class="button-unstyle big-button button">
            <NavLink to={`/main/simpleTeamCreation`} className="link-unstyle" activeClassName="active">
              Essayez un mois gratuitement
            </NavLink>
          </button>
        </div>
        <span class="tip" style={{margin: '10px 0 50px 0'}}>Pas de carte de crédit requise</span>
        <div class="display-flex flex_direction_column">
          <div class="feature">
            <i class="fa fa-check tic"/>
            <span>Rooms illimitées</span>
          </div>
          <div class="feature">
            <i class="fa fa-check tic"/>
            <span>Partage d'accès web avec visualisation ou obstruction des mots de passe</span>
          </div>
          <div class="feature">
            <i class="fa fa-check tic"/>
            <span>Partage d'outils SaaS avec personnalisation des accès pour chaque utilisateur</span>
          </div>
          <div class="feature">
            <i class="fa fa-check tic"/>
            <span>Partage d'identifiants pour softwares en local (coming soon)</span>
          </div>
          <div class="feature">
            <i class="fa fa-check tic"/>
            <span>Partage temporaire d'identifiants (coming soon)</span>
          </div>
          <div class="feature">
            <i class="fa fa-check tic"/>
            <span>Retrait automatique des accès à date paramétrée</span>
          </div>
          <div class="feature">
            <i class="fa fa-check tic"/>
            <span>Plusieurs Admins et 1 Owner</span>
          </div>
          <div class="feature">
            <i class="fa fa-check tic"/>
            <span>Intégration Slack (coming soon)</span>
          </div>
          <div class="feature">
            <i class="fa fa-check tic"/>
            <span>Priority support</span>
          </div>
        </div>
        <div class="text-center" style={{margin: '60px 0 10px 0'}}>
          <button class="button-unstyle big-button button">
            <NavLink to={`/main/simpleTeamCreation`} className="link-unstyle" activeClassName="active">
              Essayez un mois gratuitement
            </NavLink>
          </button>
        </div>
        <span class="tip">Pas de CB requise</span>
      </div>
  )
}

function TeamEnterprisePlan(props){
  return (
      <div class="team_plan" id="enterprise_team_plan">
        <img src="/resources/other/Saturn.svg" alt="icon" class="styleImage"/>
        <div class="plan_header display-flex flex_direction_column text-center">
          <h1 class="text-center title">Entreprise</h1>
          <span style={{fontSize: '0.9rem'}}>à partir de</span>
          <span class="text-center price" style={{marginBottom: '33px'}}>7,89 <span class="symbol">€HT</span></span>
        </div>
        <span class="info">L'integralité de pro<br/>et des fonctionnalités à la demande</span>
        <div class="display-flex flex_direction_column full_flex">
          <div class="feature">
            <i class="fa fa-check tic"/>
            <span>Intégration Single Sign-on (SSO)</span>
          </div>
          <div class="feature">
            <i class="fa fa-check tic"/>
            <span>Authentification à double facteur</span>
          </div>
          <div class="feature">
            <i class="fa fa-check tic"/>
            <span>Déploiment applicatif et base de données en interne</span>
          </div>
          <div class="feature">
            <i class="fa fa-check tic"/>
            <span>Synchronisation temps-réel avec votre Active Directory</span>
          </div>
          <div class="feature">
            <i class="fa fa-check tic"/>
            <span>Conservation et gestion d'archives</span>
          </div>
          <div class="feature">
            <i class="fa fa-check tic"/>
            <span>Temps de fonctionnement disponible (SLA): 99%</span>
          </div>
        </div>
        <div class="text-center">
          <button class="button-unstyle big-button button">
            <a href="/companyContact" class="link-unstyle">
              Contactez-nous
            </a>
          </button>
        </div>
      </div>
  )
}

function Step2(props){
  return (
      <div class="content display-flex flex_direction_column marginAuto step2">
        <h1 class="text-center" style={{margin: '0 0 10px 0'}}>Un prix juste et transparent</h1>
        <span class="sub-title">Offre <u>sans engagement</u>, facturée <u>mensuellement</u> par utilisateur <u>actif</u>*</span>
        <div class="display-flex" style={{margin: '55px 0 37px 0'}}>
          <TeamProPlan/>
          <TeamEnterprisePlan/>
        </div>
        <span class="sub-title">* un utilisateur est actif à partir du moment où il a reçu ou envoyé au moins une App dans votre Team Space.</span>
      </div>
  )
}

class TeamsPreview extends React.Component {
  constructor(props){
    super(props);
    this.state = {
      passed: false
    };
    this.setPassed = this.setPassed.bind(this);
  }
  setPassed(state){
    this.setState({passed: state});
  }
  render(){
    return(
        <div id="teamsPreview" class="display-flex justify_content_center align_items_center">
          {!this.state.passed ?
              <Step1 passStep={this.setPassed}/>
              :
              <Step2/>
          }
        </div>
    )
  }
}

module.exports = TeamsPreview;