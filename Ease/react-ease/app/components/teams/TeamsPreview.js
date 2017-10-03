import React from 'react';
import {NavLink} from 'react-router-dom';
import {Icon, Popup} from 'semantic-ui-react';

const TeamPlanPopup = ({text, trigger}) => {
    return (
        <Popup size="mini"
               position="top center"
               inverted
               id="team_plan_popup"
               trigger={
                   trigger
               }
               content={text}/>
    )
};

function Step1(props){
  return (
      <div class="content display-flex marginAuto step1" style={{width: '900px'}}>
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

function TeamBasicPlan(props) {
    return (
        <div class="team_plan" id="starter_plan" style={{marginRight: '40px'}}>
            <h1 class="text-center title">Starter</h1>
            <span class="text-center price">0 <span class="symbol">€</span></span>
            <span class="price_divider">per month, per active user</span>
            <div class="text-center" style={{marginBottom: '35px'}}>
                <button class="button-unstyle big-button button">
                    <NavLink to={`/main/simpleTeamCreation?plan_id=0`} className="link-unstyle"
                             activeClassName="active">
                        CREATE A FREE TEAM
                    </NavLink>
                </button>
            </div>
            <span class="info">INCLUDED IN THE <TeamPlanPopup text={'to handle passwords accessible by team members'}
                                                              trigger={<span
                                                                  class="text-underlined">TEAM SPACE</span>}/> <TeamPlanPopup
                text={'to handle passwords accessible by team members'}
                trigger={<Icon link name="help circle outline"/>}/></span>
            <div class="display-flex flex_direction_column full_flex">
                <div class="feature">
                    <i class="fa fa-check tic"/>
                    <span>Up to 30 team members</span>
                </div>
                <div class="feature">
                    <i class="fa fa-check tic"/>
                    <span>One to one & group password sharing</span>
                </div>
                <div class="feature">
                    <i class="fa fa-check tic"/>
                    <span>Unlimited team passwords</span>
                </div>
                <div class="feature">
                    <i class="fa fa-check tic"/>
                    <span>Up to 4 <TeamPlanPopup
                        text={'Rooms group the people  who work together so sharing & organizing passwords is easier'}
                        trigger={<span class="text-underlined">rooms</span>}/></span>
                </div>
                <div class="feature">
                    <i class="fa fa-check tic"/>
                    <span>1 <TeamPlanPopup text={'Admins can activate, deactivate members, give them access to tools…'}
                                           trigger={<span class="text-underlined">Admin</span>}/></span>
                </div>
                <div class="feature">
                    <i class="fa fa-check tic"/>
                    <span><TeamPlanPopup
                        text={'Notifications makes it all work smoothly, it simplifies interacting with team members'}
                        trigger={<span class="text-underlined">Notifications</span>}/></span>
                </div>
            </div>
            <div class="text-center" style={{margin: '60px 0 0 0'}}>
                <button class="button-unstyle big-button button">
                    <NavLink to={`/main/simpleTeamCreation?plan_id=0`} className="link-unstyle"
                             activeClassName="active">
                        CREATE A FREE TEAM
                    </NavLink>
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
          <span class="text-center price">3,99 <span class="symbol">€HT</span></span>
          <span class="price_divider">per month, per active user</span>
        <div class="text-center">
          <button class="button-unstyle big-button button">
              <NavLink to={`/main/simpleTeamCreation?plan_id=1`} className="link-unstyle" activeClassName="active">
                  TRY 1 MONTH FREE
            </NavLink>
          </button>
        </div>
          <span class="tip" style={{margin: '5px 0 13px 0'}}>No credit card required</span>
          <span class="info">ALL FEATURES FROM STARTER PLAN, AND:</span>
        <div class="display-flex flex_direction_column">
          <div class="feature">
            <i class="fa fa-check tic"/>
              <span>Unlimited <TeamPlanPopup
                  text={'Rooms group the people  who work together so sharing & organizing passwords is easier'}
                  trigger={<span class="text-underlined">rooms</span>}/></span>
          </div>
          <div class="feature">
            <i class="fa fa-check tic"/>
              <span>Unlimited team members</span>
          </div>
          <div class="feature">
            <i class="fa fa-check tic"/>
              <span>Unlimited admins and 1 owner</span>
          </div>
          <div class="feature">
            <i class="fa fa-check tic"/>
              <span><TeamPlanPopup
                  text={'Room managers are responsible to administer people and apps accesses within a room'}
                  trigger={<span class="text-underlined">Room Managers</span>}/> set up</span>
          </div>
          <div class="feature">
            <i class="fa fa-check tic"/>
              <span>Automatic <TeamPlanPopup
                  text={'You can set up departure dates on members to remove accesses automatically'}
                  trigger={<span class="text-underlined">access removal</span>}/></span>
          </div>
          <div class="feature">
            <i class="fa fa-check tic"/>
            <span>Retrait automatique des accès à date paramétrée</span>
          </div>
          <div class="feature">
            <i class="fa fa-check tic"/>
              <span>Priority customer support</span>
          </div>
          <div class="feature">
            <i class="fa fa-check tic"/>
              <span>Temporary password sharing</span>
          </div>
          <div class="feature">
            <i class="fa fa-check tic"/>
              <span>Software credential sharing</span>
          </div>
            <div class="feature">
                <i class="fa fa-check tic"/>
                <span>2FA login</span>
          </div>
        </div>
        <div class="text-center" style={{margin: '60px 0 10px 0'}}>
          <button class="button-unstyle big-button button">
              <NavLink to={`/main/simpleTeamCreation?plan_id=1`} className="link-unstyle" activeClassName="active">
                  TRY 1 MONTH FREE
            </NavLink>
          </button>
        </div>
          <span class="tip">No credit card required</span>
      </div>
  )
}

function TeamEnterprisePlan(props){
  return (
      <div class="team_plan" id="enterprise_team_plan">
        <img src="/resources/other/Saturn.svg" alt="icon" class="styleImage"/>
        <div class="plan_header display-flex flex_direction_column text-center">
            <h1 class="text-center title" style={{marginBottom: '25px'}}>Premium</h1>
            <span class="text-center price">7,89 <span class="symbol">€HT</span></span>
            <span style={{fontSize: '0.8rem', marginTop: '10px'}}>per month, per active user</span>
        </div>
          <span class="info">ALL FEATURES FROM STARTER, PRO, AND:</span>
        <div class="display-flex flex_direction_column full_flex">
          <div class="feature">
            <i class="fa fa-check tic"/>
              <span><TeamPlanPopup text={'Companies using an internal SSO can integrate Ease.space'} trigger={<span
                  class="text-underlined">Single Sign-on</span>}/> integration (SSO)</span>
          </div>
          <div class="feature">
            <i class="fa fa-check tic"/>
              <span>In house database</span>
          </div>
          <div class="feature">
            <i class="fa fa-check tic"/>
              <span>Active Directory synchronisation</span>
          </div>
          <div class="feature">
            <i class="fa fa-check tic"/>
              <span>Activity logs</span>
          </div>
          <div class="feature">
            <i class="fa fa-check tic"/>
              <span>Archives</span>
          </div>
          <div class="feature">
            <i class="fa fa-check tic"/>
              <span>99.8% SLA</span>
          </div>
        </div>
        <div class="text-center">
          <button class="button-unstyle big-button button">
            <a href="/companyContact" class="link-unstyle">
                CONTACT US
            </a>
          </button>
        </div>
      </div>
  )
}

function Step2(props){
  return (
      <div class="content display-flex flex_direction_column marginAuto step2">
          <h1 class="text-center" style={{margin: '0 0 10px 0'}}>Fair and transparent pricing</h1>
          <span class="sub-title">Monthly billing per active user, cancel any time</span>
        <div class="display-flex" style={{margin: '55px 0 37px 0'}}>
            <TeamBasicPlan/>
          <TeamProPlan/>
          <TeamEnterprisePlan/>
        </div>
          <span
              class="sub-title">* a user is active from the day he/she shares or receives an app in his/her Team Space</span>
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