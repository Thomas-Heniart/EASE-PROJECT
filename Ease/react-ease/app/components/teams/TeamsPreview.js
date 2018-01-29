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
          <h3 style={{fontSize:'24px', marginBottom: '40px'}}>You can also share, organise and secure company passwords with Ease.space.</h3>
          <span style={{lineHeight:'1.78', color: '#838593'}}>Setting up your team will take less than 10 minutes and you don’t need to do it all at once. Enjoy !</span>
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
      <div class="team_plan_wrapper" style={{marginRight: '40px'}}>
        <div class="team_plan" id="starter_plan">
          <h1 class="text-center title">Basic</h1>
          <span class="text-center price">0 <span class="symbol">€</span></span>
          <span class="price_divider">per month</span>
          <div class="text-center">
            <button class="button-unstyle big-button button">
              <NavLink to={`/main/simpleTeamCreation?plan_id=0`} className="link-unstyle"
                       activeClassName="active">
                CREATE A FREE TEAM
              </NavLink>
            </button>
          </div>
        </div>
        <div class="features first display-flex flex_direction_column full_flex">
          <div class="feature">
            <i class="fa fa-check tic"/>
            <span>Unlimited shared passwords</span>
          </div>
          <div class="feature">
            <i class="fa fa-check tic"/>
            <span>Unlimited enterprise passwords</span>
          </div>
          <div class="feature">
            <i class="fa fa-check tic"/>
            <span>Unlimited <TeamPlanPopup
                text={'Rooms group the people  who work together so sharing & organizing passwords is easier'}
                trigger={<span class="text-underlined">rooms</span>}/></span>
          </div>
          <div class="feature">
            <i class="fa fa-check tic"/>
            <span><TeamPlanPopup
                text={'Notifications makes it all work smoothly, it simplifies interacting with team members'}
                trigger={<span class="text-underlined">Notifications</span>}/></span>
          </div>
          <div class="feature">
            <i class="fa fa-check tic"/>
            <span>Multi-device <TeamPlanPopup
                text={'Your account is automatically synchronized across your devices.'}
                trigger={<span class="text-underlined">synchronization</span>}/></span>
          </div>
        </div>
        <div class="features first display-flex flex_direction_column full_flex">
          <div class="feature">
            <i class="fa fa-check tic"/>
            <span>Up to 15 employees</span>
          </div>
          <div class="feature">
            <i class="fa fa-check tic"/>
            <span>Up to 1 Team <TeamPlanPopup text={'Admins can activate, deactivate members, give them access to tools…'}
                                              trigger={<span class="text-underlined">Admin</span>}/></span>
          </div>
        </div>
      </div>
  )
}

function TeamProPlan(props){
  return (
      <div  style={{marginRight: '40px'}} class="team_plan_wrapper">
        <div class="team_plan" id="pro_team_plan">
          <img src="/resources/other/illu.svg" alt="icon" class="styleImage"/>
          <h1 class="text-center title">Pro</h1>
          <span class="text-center price">59 <span class="symbol">€HT</span></span>
          <span class="price_divider">per month</span>
          <div class="text-center">
            <button class="button-unstyle big-button button">
              <NavLink to={`/main/simpleTeamCreation?plan_id=1`} className="link-unstyle" activeClassName="active">
                TRY 1 MONTH FREE
              </NavLink>
            </button>
          </div>
          <span class="tip" style={{margin: '5px 0 13px 0'}}>No credit card required</span>
        </div>
        <div class="features first display-flex flex_direction_column full_flex">
          <div class="feature">
            <i class="fa fa-check tic"/>
            <span>Unlimited shared passwords</span>
          </div>
          <div class="feature">
            <i class="fa fa-check tic"/>
            <span>Unlimited enterprise passwords</span>
          </div>
          <div class="feature">
            <i class="fa fa-check tic"/>
            <span>Unlimited <TeamPlanPopup
                text={'Rooms group the people  who work together so sharing & organizing passwords is easier'}
                trigger={<span class="text-underlined">rooms</span>}/></span>
          </div>
          <div class="feature">
            <i class="fa fa-check tic"/>
            <span><TeamPlanPopup
                text={'Notifications makes it all work smoothly, it simplifies interacting with team members'}
                trigger={<span class="text-underlined">Notifications</span>}/></span>
          </div>
          <div class="feature">
            <i class="fa fa-check tic"/>
            <span>Multi-device <TeamPlanPopup
                text={'Your account is automatically synchronized across your devices.'}
                trigger={<span class="text-underlined">synchronization</span>}/></span>
          </div>
        </div>
        <div class="features display-flex flex_direction_column full_flex">
          <div class="feature">
            <i class="fa fa-check tic"/>
            <span>Unlimited employees</span>
          </div>
          <div class="feature">
            <i class="fa fa-check tic"/>
            <span>Hierarchical access control</span>
          </div>
          <div class="feature">
            <i class="fa fa-check tic"/>
            <span>Employee's <TeamPlanPopup
                text={'Set up people’s arrival and departure dates to send or revoke accesses automatically.'}
                trigger={<span class="text-underlined">on/off boarding</span>}/></span>
          </div>
          <div class="feature">
            <i class="fa fa-check tic"/>
            <span>Password modification policy</span>
          </div>
        </div>
      </div>
  )
}

function TeamEnterprisePlan(props){
  return (
      <div class="team_plan_wrapper">
        <div class="team_plan" id="enterprise_team_plan">
          <img src="/resources/other/Saturn.svg" alt="icon" class="styleImage"/>
          <h1 class="text-center title">Premium</h1>
          <span class="text-center price">299 <span class="symbol">€HT</span></span>
          <span class="price_divider">per month</span>
          <div class="text-center">
            <button class="button-unstyle big-button button">
              <a href="/companyContact" class="link-unstyle">
                CONTACT US
              </a>
            </button>
          </div>
        </div>
        <div class="features first display-flex flex_direction_column full_flex">
          <div class="feature">
            <i class="fa fa-check tic"/>
            <span>Unlimited shared passwords</span>
          </div>
          <div class="feature">
            <i class="fa fa-check tic"/>
            <span>Unlimited enterprise passwords</span>
          </div>
          <div class="feature">
            <i class="fa fa-check tic"/>
            <span>Unlimited <TeamPlanPopup
                text={'Rooms group the people  who work together so sharing & organizing passwords is easier'}
                trigger={<span class="text-underlined">rooms</span>}/></span>
          </div>
          <div class="feature">
            <i class="fa fa-check tic"/>
            <span><TeamPlanPopup
                text={'Notifications makes it all work smoothly, it simplifies interacting with team members'}
                trigger={<span class="text-underlined">Notifications</span>}/></span>
          </div>
          <div class="feature">
            <i class="fa fa-check tic"/>
            <span>Multi-device <TeamPlanPopup
                text={'Your account is automatically synchronized across your devices.'}
                trigger={<span class="text-underlined">synchronization</span>}/></span>
          </div>
        </div>
        <div class="features display-flex flex_direction_column full_flex">
          <div class="feature">
            <i class="fa fa-check tic"/>
            <span>Unlimited employees</span>
          </div>
          <div class="feature">
            <i class="fa fa-check tic"/>
            <span>Hierarchical access control</span>
          </div>
          <div class="feature">
            <i class="fa fa-check tic"/>
            <span>Employee's <TeamPlanPopup
                text={'Set up people’s arrival and departure dates to send or revoke accesses automatically.'}
                trigger={<span class="text-underlined">on/off boarding</span>}/></span>
          </div>
          <div class="feature">
            <i class="fa fa-check tic"/>
            <span>Password modification policy</span>
          </div>
        </div>
        <div class="features first display-flex flex_direction_column full_flex">
          <div class="feature">
            <i class="fa fa-check tic"/>
            <span>Mandatory 2FA</span>
          </div>
          <div class="feature">
            <i class="fa fa-check tic"/>
            <span>Time specific password sharing</span>
          </div>
          <div class="feature">
            <i class="fa fa-check tic"/>
            <span>Geolocated access control</span>
          </div>
          <div class="feature">
            <i class="fa fa-check tic"/>
            <span>Securely receive external passwords</span>
          </div>
          <div class="feature">
            <i class="fa fa-check tic"/>
            <span>On-premise database</span>
          </div>
          <div class="feature">
            <i class="fa fa-check tic"/>
            <span>AD-LDAP integration</span>
          </div>
          <div class="feature">
            <i class="fa fa-check tic"/>
            <span>Activity Logs</span>
          </div>
        </div>
      </div>
  )
}

function Step2(props){
  return (
      <div class="content display-flex flex_direction_column marginAuto step2">
        <h1 class="text-center" style={{margin: '0 0 10px 0'}}>Before creating your team, please choose a plan.</h1>
        <span class="sub-title">Monthly billing, cancellable anytime</span>
        <div class="display-flex" style={{margin: '55px 0 37px 0'}}>
          <TeamBasicPlan/>
          <TeamProPlan/>
          <TeamEnterprisePlan/>
        </div>
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
          <Step2/>
        </div>
    )
  }
}

module.exports = TeamsPreview;