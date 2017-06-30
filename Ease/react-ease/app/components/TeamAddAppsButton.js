var React = require('react');
var classnames = require('classnames');
import {showAppAddUIElement} from "../actions/teamAppsAddUIActions"
import {connect} from "react-redux"

@connect()
class TeamAddAppsButton extends React.Component {
  constructor(props){
    super(props);
    this.state = {
      dropdown: false
    };
    this.showDropdown = this.showDropdown.bind(this);
    this.onMouseDown = this.onMouseDown.bind(this);
    this.onMouseUp = this.onMouseUp.bind(this);
    this.pageClick = this.pageClick.bind(this);
    this.selectItem = this.selectItem.bind(this);
  }
  selectItem(type){
    this.props.dispatch(showAppAddUIElement(type, true));
    this.showDropdown(false);
  }
  showDropdown(state){
    if (state === this.state.dropdown)
      return;
    this.setState({dropdown: state});
  }
  onMouseDown(){
    this.mouseInDropDown = true;
  }
  onMouseUp(){
    this.mouseInDropDown = false;
  }
  pageClick(e){
    if (this.mouseInDropDown)
      return;
    this.setState({dropdown: false});
  }
  componentDidMount(){
    window.addEventListener('mousedown', this.pageClick, false);
  }
  componentWillUnmount(){
    window.removeEventListener('mousedown', this.pageClick, false);
  }
  render(){
    return (
        <button class="button-unstyle" id="add_team_apps_button"
                onMouseDown={this.onMouseDown}
                onMouseUp={this.onMouseUp}
                onClick={this.showDropdown.bind(null, true)}>
          <i class="fa fa-plus"/>
          <div class={classnames('floating_dropdown', this.state.dropdown ? 'show': null)}>
            <div class="dropdown_content">
              <div class="dropdown_row selectable" onClick={this.selectItem.bind(null,'Simple')}>
                <i class="fa fa-square"/>
                <span>Simple app</span>
              </div>
              <div class="dropdown_row selectable" onClick={this.selectItem.bind(null,'Multi')}>
                <i class="fa fa-sitemap"/>
                <span>Entreprise app</span>
              </div>
              <div class="dropdown_row selectable" onClick={this.selectItem.bind(null, 'Link')}>
                <i class="fa fa-link"/>
                <span>Link app</span>
              </div>
            </div>
          </div>
        </button>
    )
  }
}

module.exports = TeamAddAppsButton;