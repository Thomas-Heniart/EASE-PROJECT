import React from 'react';
import { Grid, Image, Icon, Modal } from 'semantic-ui-react';
import {handleSemanticInput} from "../../utils/utils";
import ClassicAppModal from './ClassicAppModal';
import SsoAppModal from './SsoAppModal';

class ShowGrid extends React.Component {

    constructor(props){
        super(props);
    }

    // handleInput = handleSemanticInput.bind(this);

    render() {

        let appsSorted = this.props.apps.filter((item) => {
            if (item.category_id && this.props.categorySelected)
                return item.category_id === this.props.categorySelected;
            else
                return item;
        });

        return (
            <Grid columns={4} className="logoCatalog">
                {!appsSorted.length ?
                    <div/>
                    :
                    appsSorted.map((item, key) =>
                        <Modal key={key} trigger={
                            <Grid.Column as='a' className="showSegment" onClick={e => console.log('replace')}>
                                <Image src={item.logo}/>
                                <p>{item.name}</p>
                                <Icon name="add square"/>
                            </Grid.Column>} >
                            {item.sso_id ?
                                <SsoAppModal url={item.landing_url} logo={item.logo} name={item.name}
                                             connectWith_websites={item.connectWith_websites} />
                                :
                                <ClassicAppModal url={item.landing_url} logo={item.logo} name={item.name}
                                                 connectWith_websites={item.connectWith_websites} />
                            }
                            </Modal>
                    )
                }
            </Grid>
        )
    }
}

export default ShowGrid;