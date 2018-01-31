import React from 'react';
import {Header, Icon, Button} from 'semantic-ui-react';
import {showNewFeatureModal} from "../../actions/modalActions";
import {connect} from "react-redux";
import {newFeatureSeen} from "../../actions/commonActions";
import Carousel from "nuka-carousel";
import createReactClass from "create-react-class";

const CarouselContent = createReactClass({
  render(){
    let decorators = [{
      component: createReactClass({
        render(){
          return (
              <Icon size="big" class="mrgn0" name="chevron circle left" link onClick={this.props.previousSlide}/>
          )
        }
      }),
      position: 'CenterLeft'
    }, {
      component: createReactClass({
        render(){
          return (
              <Icon size="big" class="mrgn0" name="chevron circle right" link onClick={this.props.nextSlide}/>
          )
        }
      }),
      position: 'CenterRight'
    },{
      component: createReactClass({
        render() {
          var self = this;
          var indexes = this.getIndexes(self.props.slideCount, self.props.slidesToScroll);
          return (
              <ul style={self.getListStyles()}>
                {
                  indexes.map(function(index) {
                    return (
                        <li style={self.getListItemStyles()} key={index}>
                          <button
                              style={self.getButtonStyles(self.props.currentSlide === index)}
                              onClick={self.props.goToSlide.bind(null, index)}>
                            &bull;
                          </button>
                        </li>
                    )
                  })
                }
              </ul>
          )
        },
        getIndexes(count, inc) {
          var arr = [];
          for (var i = 0; i < count; i += inc) {
            arr.push(i);
          }
          return arr;
        },
        getListStyles() {
          return {
            position: 'relative',
            margin: 0,
            top: -10,
            padding: 0
          }
        },
        getListItemStyles() {
          return {
            listStyleType: 'none',
            display: 'inline-block'
          }
        },
        getButtonStyles(active) {
          return {
            border: 0,
            background: 'transparent',
            color: 'black',
            cursor: 'pointer',
            padding: 10,
            outline: 0,
            fontSize: 24,
            opacity: active ? 1 : 0.5
          }
        }
      }),
      position: 'BottomCenter'
    }
    ];
    return (
        <Carousel
            decorators={decorators}
            wrapAround={true}
            autoplay={true}
            autoplayInterval={1000}>
          <img src="/resources/images/chrome_import.jpg" onLoad={() => {window.dispatchEvent(new Event('resize'));}}/>
          <img src="/resources/images/extension_popup.jpg"/>
          <img src="/resources/images/google_accounts.jpg"/>
        </Carousel>
    )
  }
});

class NewFeatureModal extends React.Component {
  constructor(props){
    super(props);
  };
  close = () => {
    this.props.dispatch(newFeatureSeen());
    this.props.dispatch(showNewFeatureModal({active: false}));
  };
  render() {
    return (
        <div className="popupHandler myshow">
          <div className="popover_mask"/>
          <div className="ease_popup ease_new_feature_popup">
            <Header as="h3" attached="top" style={{padding: '20px'}}>
              <p>Want to check the last features we made for you?</p>
            </Header>
            <div className='popup_content'>
              <CarouselContent/>
              <div style={{display: 'flex', position: 'relative',padding: '20px', justifyContent:'space-between'}} class="align_items_center">
                <p className='no_margin'>
              <span>
                {/*<strong>If you wanna try and give us your <a href='mailto:victor@ease.space' target="_top" style={{color: 'black'}}><u>feedbacks</u></a>, it helps us a lot <Icon name="heart"/></strong><br/>*/}
                <strong>If you love Ease.space, share it with us on <a href='https://twitter.com/intent/tweet?text=%40ease_space%20%23thiseaselove' target="_blank" style={{color: 'black'}}><u>Twitter</u></a> <Icon name="heart"/></strong>
              </span>
                </p>
                <Button positive content='Okay cheers!' onClick={this.close}/>
              </div>
            </div>
          </div>
        </div>
    )
  }
};

export default connect(store => ({
  modal: store.modals
}))(NewFeatureModal);